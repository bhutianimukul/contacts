package com.project.contacts.contacts.Services;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import com.project.contacts.contacts.Models.Dto.UserDto;
import com.project.contacts.contacts.Models.Entities.UserEntity;
import com.project.contacts.contacts.Utilities.JWTUtils;
import com.project.contacts.contacts.repositories.UserRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserServices {
    @Autowired
    UserRepository repo;
    @Autowired
BCryptPasswordEncoder bcrypt;
// @Autowired
// AuthenticationManager authenticationManager;
@Autowired
JWTUtils jwt;

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
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = repo.findByEmail(email);
        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        System.out.println(userEntity.getEmail());
        System.out.println(userEntity.getEncryptedPassword());
        return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    
}
