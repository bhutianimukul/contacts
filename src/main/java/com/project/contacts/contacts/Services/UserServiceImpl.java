package com.project.contacts.contacts.Services;

import com.project.contacts.contacts.Models.Dto.UserDto;
import com.project.contacts.contacts.Models.Entities.UserEntity;
import com.project.contacts.contacts.repositories.UserRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class UserServiceImpl implements UserServices {
    @Autowired
    UserRepository repo;
    @Autowired
BCryptPasswordEncoder bcrypt;
    @Override
    public UserDto signupUser(UserDto user) {
UserEntity foundUser=repo.findByEmail(user.getEmail());
if(foundUser!=null){
    throw new RuntimeException("User already signed up");
}
    user.setEncryptedPassword(bcrypt.encode(user.getPassword()));
    UserEntity userEntity=new UserEntity();
    BeanUtils.copyProperties(user,userEntity );
    repo.save(userEntity);
        return user;
    }
    
}
