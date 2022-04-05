package com.project.contacts.contacts.Services;

import java.util.ArrayList;
import java.util.List;

import com.project.contacts.contacts.Models.Contacts.ContactModel;
import com.project.contacts.contacts.Models.Entities.ContactEntity;
import com.project.contacts.contacts.Models.Entities.UserEntity;
import com.project.contacts.contacts.Utilities.Utils;
import com.project.contacts.contacts.repositories.ContactRepository;
import com.project.contacts.contacts.repositories.UserRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    ContactRepository repo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    Utils util;

    @Override
    public List<ContactModel> getAllContacts(String userId) {
        UserEntity user = userRepo.findByUserId(userId);
        List<ContactModel> list = new ArrayList<>();
        for (ContactEntity e : user.getContacts()) {
            list.add(new ContactModel(e.getContactId(), e.getName(), e.getPhoneNo(), e.getEmail(), e.getImage(),
                    e.getCategory()));
        }
        return list;
    }

    @Override
    public void addContact(ContactModel contact, String userId) {
        UserEntity user = userRepo.findByUserId(userId);
        contact.setContactId(util.generateUserId(30));
        ContactEntity ce = new ContactEntity();

        BeanUtils.copyProperties(contact, ce);
        ce.setUser(user);
        repo.save(ce);

    }

    @Override
    public ContactModel getContactById(String contactId) {
        ContactModel contact = new ContactModel();
        ContactEntity contactEntity = repo.findByContactId(contactId);
        if (contactEntity == null)
            try {
                throw new Exception("Contact not found");
            } catch (Exception e) {
                e.printStackTrace();
            }
        BeanUtils.copyProperties(contactEntity, contact);
        return contact;

    }

}
