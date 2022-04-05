package com.project.contacts.contacts.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

import com.project.contacts.contacts.Models.Contacts.ContactModel;
import com.project.contacts.contacts.Services.ContactsService;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    @Autowired
    ContactsService service;

    @GetMapping("/getContacts")
    public List<ContactModel> getAllContactByUserId(@RequestParam String userId) {

        return service.getAllContacts(userId);
    }

    @PostMapping("/addContact")
    public String addContact(@RequestBody ContactModel model, @RequestParam String userId) {

        service.addContact(model, userId);

        return "user Added";
    }
}
