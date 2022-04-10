package com.project.contacts.contacts.Services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.project.contacts.contacts.Models.Contacts.ContactModel;
import com.project.contacts.contacts.Models.Dto.UserDto;
import com.project.contacts.contacts.Models.Entities.ContactEntity;
import com.project.contacts.contacts.Models.Entities.UserEntity;
import com.project.contacts.contacts.Utilities.Utils;
import com.project.contacts.contacts.repositories.ContactRepository;
import com.project.contacts.contacts.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    ContactRepository repo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    Utils util;

    @Override
    public List<ContactModel> getAllContacts(String userId, int page, int limit) {
        PageRequest pg = PageRequest.of(page, limit);
        Page<ContactEntity> paging = repo.findAllContacts(userId, pg);
        List<ContactEntity> contacts = paging.getContent();
        List<ContactModel> list = new ArrayList<>();
        for (ContactEntity e : contacts) {
            list.add(new ContactModel(e.getContactId(), e.getName(), e.getPhoneNo(), e.getEmail(), e.getImage(),
                    e.getCategory(), e.getDescription()));
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

    @Override
    public String deleteContactOnce(String contactId, String userId) throws Exception {

        try {
            UserEntity user = userRepo.findByUserId(userId);
            List<ContactEntity> contacts = user.getContacts();

            for (ContactEntity ce : contacts) {
                if (ce.getContactId().equals(contactId)) {
                    contacts.remove(ce);
                }
            }
            user.setContacts(contacts);
            userRepo.save(user);

        } catch (Exception e) {
            throw new Exception("Contact Not Found");
        }
        return contactId;
    }

    @Override
    public void deleteContactMultiple(List<String> list, String userId) throws Exception {

        try {
            repo.deleteAllById(list);
        } catch (Exception e) {
            throw new Exception("Contact Not Found");
        }
    }

    @Override
    @Modifying
    @Transactional

    public boolean updateContact(UserDto userDto, ContactModel newContact, String contactId) {
        try {
            ContactEntity oldContact = repo.findContactsByIdAndUserId(contactId, userDto.getUserId());
            // BeanUtils.copyProperties(newContact, oldContact);

            ContactEntity updatedContact = new ContactEntity();
            BeanUtils.copyProperties(oldContact, updatedContact);
            if (newContact.getName() != null)
                updatedContact.setName(newContact.getName());
            if (newContact.getPhoneNo() != null)
                updatedContact.setPhoneNo(newContact.getPhoneNo());
            if (newContact.getCategory() != null)
                updatedContact.setCategory(newContact.getCategory());
            if (newContact.getDescription() != null)
                updatedContact.setDescription(newContact.getDescription());
            if (newContact.getImage() != null)
                updatedContact.setImage(newContact.getImage());
            if (newContact.getEmail() != null)
                updatedContact.setEmail(newContact.getEmail());
            repo.save(updatedContact);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
