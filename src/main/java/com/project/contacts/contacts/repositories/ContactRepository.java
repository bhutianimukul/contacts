package com.project.contacts.contacts.repositories;

import com.project.contacts.contacts.Models.Entities.ContactEntity;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

// DAO  
@Repository
public interface ContactRepository extends PagingAndSortingRepository<ContactEntity, String> {
    public ContactEntity findByContactId(String contactId);
}
