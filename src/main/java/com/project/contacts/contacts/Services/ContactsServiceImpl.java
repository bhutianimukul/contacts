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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
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
    public List<ContactModel> getAllContacts(String userId, int page, int limit) {
        PageRequest pg = PageRequest.of(page, limit);
        Page<ContactEntity> paging = repo.findAllContacts(userId, pg);
        List<ContactEntity> contacts = paging.getContent();
        List<ContactModel> list = new ArrayList<>();
        for (ContactEntity e : contacts) {
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
    public boolean updateContact(UserDto userDto, ContactModel contact) {
        try {
            UserEntity userEntity = userRepo.findByUserId(userDto.getUserId());

            List<ContactEntity> list = userEntity.getContacts();
            System.out.println(contact);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getContactId() == contact.getContactId()) {
                    ContactEntity c = list.remove(i);
                    BeanUtils.copyProperties(contact, c);
                    System.out.println(c);
                    list.add(c);
                    break;
                }
            }
            userEntity.setContacts(list);
            userRepo.save(userEntity);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
