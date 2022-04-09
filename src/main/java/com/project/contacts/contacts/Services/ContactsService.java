package com.project.contacts.contacts.Services;

import org.springframework.stereotype.Service;
import java.util.*;

import com.project.contacts.contacts.Models.Contacts.ContactModel;
import com.project.contacts.contacts.Models.Dto.UserDto;

@Service
public interface ContactsService {
    public List<ContactModel> getAllContacts(String userId, int page, int limit);

    public void addContact(ContactModel contact, String userId);

    public ContactModel getContactById(String contactId);

    public String deleteContactOnce(String contactId, String userId) throws Exception;

    public void deleteContactMultiple(List<String> list, String userId) throws Exception;

    public boolean updateContact(UserDto userDto, ContactModel user);

}
