package com.project.contacts.contacts.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.*;

import javax.transaction.Transactional;

import com.project.contacts.contacts.Models.ResponseMessageModel;
import com.project.contacts.contacts.Models.Contacts.ContactModel;
import com.project.contacts.contacts.Models.Dto.UserDto;
import com.project.contacts.contacts.Services.ContactsService;
import com.project.contacts.contacts.Services.UserServices;
import com.project.contacts.contacts.Utilities.JWTUtils;

@RestController

@RequestMapping("/contacts")
public class ContactController {
    @Autowired
    ContactsService service;
    @Autowired
    UserServices userService;
    @Autowired
    JWTUtils jwt;

    // ! Add New Contact
    @CrossOrigin
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessageModel> addContact(@RequestBody ContactModel model, Principal principal) {
        String user = principal.getName();
        ResponseMessageModel msg = new ResponseMessageModel();
        UserDto userDto = userService.getUserByEmail(user);
        if (userDto.isEnabled() == false) {

            msg.setMessage("User Not Authenticated");

            // throw new UsernameNotFoundException("User not found");
            return new ResponseEntity<>(msg, HttpStatus.FORBIDDEN);
        }

        service.addContact(model, userDto.getUserId());

        msg.setMessage("Contact Added Successfully");
        return ResponseEntity.ok(msg);
    }

    // ! get contact By id
    @GetMapping("/get")
    @CrossOrigin
    public ResponseEntity<ContactModel> getContactById(@RequestParam String contactId) {

        return ResponseEntity.ok((service.getContactById(contactId)));
    }

    // ! Get All Contacts
    @GetMapping("/getAll")
    @CrossOrigin
    public ResponseEntity<Object> getAllContactByUserId(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit, Principal principal) {

        String user = principal.getName();
        UserDto userDto = userService.getUserByEmail(user);
        if (userDto.isEnabled() == false) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "User Not Authenticated");
            // throw new UsernameNotFoundException("User not found");
            return new ResponseEntity<Object>(map, HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(service.getAllContacts(userDto.getUserId(), page, limit));
    }

    // ! Delete One Contact
    @DeleteMapping("/delete")
    @CrossOrigin
    @Transactional
    // delete 1 contact
    public ResponseEntity<ResponseMessageModel> deleteContact(@RequestParam String contactId, Principal principal)
            throws Exception {
        ResponseMessageModel msg = new ResponseMessageModel();
        String user = principal.getName();
        UserDto userDto = userService.getUserByEmail(user);
        String contactName = service.deleteContactOnce(contactId, userDto.getUserId());

        msg.setMessage(contactName + " Contact Deleted");
        return ResponseEntity.ok(msg);

    }

    // ! DELETE Multiple Contact
    @DeleteMapping("/deleteMultiple")
    @CrossOrigin
    @Transactional
    // delete 1 contact
    public ResponseEntity<Map<String, String>> deleteMultiple(@RequestBody Map<String, List<String>> map,
            Principal principal)
            throws Exception {
        List<String> list = map.get("contacts");
        String user = principal.getName();
        UserDto userDto = userService.getUserByEmail(user);
        service.deleteContactMultiple(list, userDto.getUserId());
        Map<String, String> ans = new HashMap<>();
        ans.put("message", "Success!!!!!! Contacts Deleted");
        return ResponseEntity.ok(ans);
    }
    // ! Update Contact

    @PutMapping("/updateContact")
    @CrossOrigin
    public ResponseEntity<ResponseMessageModel> updateContact(@RequestBody ContactModel contact,
            @RequestParam String contactId, Principal principal) {
        // name image phoneno
        contact.setContactId(contactId);
        ResponseMessageModel msg = new ResponseMessageModel();
        UserDto userDto = userService.getUserByEmail(principal.getName());
        if (userDto.isEnabled() == false) {

            msg.setMessage("User Not Verified");
            // throw new UsernameNotFoundException("User not found");
            return new ResponseEntity<ResponseMessageModel>(msg, HttpStatus.FORBIDDEN);
        }
        boolean flag = service.updateContact(userDto, contact, contactId);

        // UserDto userDto = userService.getUserByEmail(principal.getName());

        if (!flag) {
            msg.setMessage("Unable to update Contact. Please try again later.");
            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        msg.setMessage("Contact Updated Successfully!!");
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
