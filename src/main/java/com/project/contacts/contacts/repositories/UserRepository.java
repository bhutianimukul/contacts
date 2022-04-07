package com.project.contacts.contacts.repositories;

import com.project.contacts.contacts.Models.Entities.UserEntity;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

// DAO  
@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, String> {
    public UserEntity findByEmail(String email);

    public UserEntity findByVerificationToken(String token);

    public UserEntity findByUserId(String userId);
}
