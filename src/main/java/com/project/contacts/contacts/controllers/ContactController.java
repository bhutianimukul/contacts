package com.project.contacts.contacts.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.*;

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

    // ! Get All Contacts
    @GetMapping("/getAll")
    public List<ContactModel> getAllContactByUserId(Principal principal) {

        String user = principal.getName();
        UserDto userDto = userService.getUserByEmail(user);
        return service.getAllContacts(userDto.getUserId());
    }

    // ! Add New Contact
    @PostMapping("/add")
    public String addContact(@RequestBody ContactModel model, Principal principal) {
        String user = principal.getName();
        UserDto userDto = userService.getUserByEmail(user);
        service.addContact(model, userDto.getUserId());
        return "user Added";
    }

    @GetMapping("/get")
    public ResponseEntity<ContactModel> getContactById(@RequestParam String contactId) {

        return ResponseEntity.ok((service.getContactById(contactId)));
    }

}
