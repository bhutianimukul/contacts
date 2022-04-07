package com.project.contacts.contacts.repositories;

import com.project.contacts.contacts.Models.Entities.ContactEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// DAO  
@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, String> {
    public ContactEntity findByContactId(String contactId);

    @Query(value = "Select * from contacts  where user_id=:id", nativeQuery = true)
    public Page<ContactEntity> findAllContacts(@Param("id") String id, Pageable pg);
}
