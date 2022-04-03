package com.project.contacts.contacts.controllers;

import com.project.contacts.contacts.Models.Dto.UserDto;
import com.project.contacts.contacts.Models.SigninModels.UserSigninRequest;
import com.project.contacts.contacts.Models.SigninModels.UserSigninResponse;
import com.project.contacts.contacts.Models.SignupModels.UserSignupRequest;
import com.project.contacts.contacts.Models.SignupModels.UserSignupResponse;
import com.project.contacts.contacts.Services.UserServices;
import com.project.contacts.contacts.Utilities.JWTUtils;
import com.project.contacts.contacts.Utilities.Utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    Utils util;
    @Autowired
    UserServices service;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTUtils jwt;

    @PostMapping("/signup")
    public UserSignupResponse userSignup(@RequestBody UserSignupRequest user) {
        UserSignupResponse res = new UserSignupResponse();
        BeanUtils.copyProperties(user, res);
        res.setUserId(util.generateUserId(30));
        UserDto dtoSend = new UserDto();
        BeanUtils.copyProperties(res, dtoSend);
        BeanUtils.copyProperties(user, dtoSend);
        UserDto userDto = service.signupUser(dtoSend);
        System.out.println(userDto.getEncryptedPassword());
        return res;

    }

    // Signin
    @PostMapping("/signin")
    public UserSigninResponse userSignin(@RequestBody UserSigninRequest user) {
        UserSigninResponse response = new UserSigninResponse();

        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        } catch (BadCredentialsException e) {

            throw new UsernameNotFoundException("User not found");
        }
        UserDto userDto = service.getUserByEmail(user.getEmail());
        String token = jwt.generateToken(user.getEmail());
        response.setUserId(userDto.getUserId());
        response.setToken(token);
        return response;
    }

    @GetMapping("/name")
    public String getName() {
        return "Mukul";
    }

}
