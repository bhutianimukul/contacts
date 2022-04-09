package com.project.contacts.contacts.Services;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.User;
import com.project.contacts.contacts.Models.Dto.UserDto;
import com.project.contacts.contacts.Models.Entities.UserEntity;
import com.project.contacts.contacts.Models.SignupModels.UserSignupRequest;
import com.project.contacts.contacts.Utilities.JWTUtils;
import com.project.contacts.contacts.Utilities.Utils;
import com.project.contacts.contacts.repositories.UserRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
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
    @Autowired
    Utils util;
    // @Autowired
    // AuthenticationManager authenticationManager;
    @Autowired
    JWTUtils jwt;
    @Autowired
    OtpService otpService;

    @Override
    public UserDto signupUser(UserDto user) {
        UserEntity foundUser = repo.findByEmail(user.getEmail());
        if (foundUser != null) {
            throw new RuntimeException("User already signed up");
        }
        user.setEncryptedPassword(bcrypt.encode(user.getPassword()));
        user.setEnabled(false);
        user.setVerificationToken(util.generateUserId(15));
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        repo.save(userEntity);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = repo.findByEmail(email);
        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserDto userDto = new UserDto();
        UserEntity userEntity = repo.findByEmail(email);
        if (userEntity == null)
            throw new UsernameNotFoundException(email);
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }

    @Override
    public void removeUser(UserDto user) throws Exception {
        try {
            repo.deleteById(user.getUserId());
        } catch (Exception e) {
            throw new Exception("Unable to remove");
        }
    }

    @Override
    public void verify(String verificationToken) throws Exception {
        // verification toekn to user
        if (verificationToken == null)
            throw new Exception("Invalid Link");
        UserEntity userEntity = repo.findByVerificationToken(verificationToken);
        // user set enable true
        userEntity.setEnabled(true);
        // verification token null
        userEntity.setVerificationToken(null);
        // save
        repo.save(userEntity);
    }

    @Override
    @Modifying
    @Transactional
    public boolean changePassword(String email, String newPassword) {
        try {
            if (otpService.getOtp(email) != 1) {
                throw new Exception();
            }
            UserEntity userEntity = repo.findByEmail(email);
            if (userEntity != null) {
                userEntity.setEncryptedPassword(bcrypt.encode(newPassword));
                // System.out.println(bcrypt.encode(newPassword).equals(userEntity.getEncryptedPassword()));
                repo.save(userEntity);
            }
        } catch (Exception e) {
            return false;
        }
        otpService.clearOTP(email);
        return true;
    }

    @Override
    @Modifying
    @Transactional
    public boolean updateUserProfile(UserDto userDto, UserSignupRequest user) {
        try {
            UserEntity userEntity = repo.findByUserId(userDto.getUserId());

            BeanUtils.copyProperties(user, userEntity);

            repo.save(userEntity);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
