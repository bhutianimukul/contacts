package com.project.contacts.contacts.Services;

import org.springframework.stereotype.Service;
import java.util.*;

import com.project.contacts.contacts.Models.Contacts.ContactModel;

@Service
public interface ContactsService {
    public List<ContactModel> getAllContacts(String userId);

    public void addContact(ContactModel contact, String userId);

    public ContactModel getContactById(String contactId);

}
