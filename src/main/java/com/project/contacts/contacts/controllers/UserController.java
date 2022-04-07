package com.project.contacts.contacts.controllers;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.project.contacts.contacts.Models.Dto.UserDto;
import com.project.contacts.contacts.Models.SigninModels.UserSigninRequest;
import com.project.contacts.contacts.Models.SigninModels.UserSigninResponse;
import com.project.contacts.contacts.Models.SignupModels.UserSignupRequest;
import com.project.contacts.contacts.Models.SignupModels.UserSignupResponse;
import com.project.contacts.contacts.Services.EmailService;
import com.project.contacts.contacts.Services.UserServices;
import com.project.contacts.contacts.Utilities.JWTUtils;
import com.project.contacts.contacts.Utilities.Utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<Object> userSignup(@RequestBody UserSignupRequest user, HttpServletRequest req)
            throws Exception {
        UserSignupResponse res = new UserSignupResponse();
        BeanUtils.copyProperties(user, res);
        res.setUserId(util.generateUserId(30));
        UserDto dtoSend = new UserDto();
        BeanUtils.copyProperties(res, dtoSend);
        BeanUtils.copyProperties(user, dtoSend);

        UserDto userDto = service.signupUser(dtoSend);

        StringBuffer url = req.getRequestURL();
        url.append("/verify/" + userDto.getVerificationToken());

        boolean mailSent = emailService.sendEmail("Verification Request", user.getEmail(), url.toString());
        if (mailSent == false) {
            service.removeUser(dtoSend);

            Map<String, String> map = new HashMap<>();
            map.put("message", "Unable to send Mail, Please try again later");

            return new ResponseEntity<Object>(map, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        res.setMessage("Email Sent Successfully!!!,Please check your mail to verify");
        System.out.println(userDto.getEncryptedPassword());
        return ResponseEntity.ok(res);

    }

    // Signin
    @PostMapping("/signin")
    public ResponseEntity<Object> userSignin(@RequestBody UserSigninRequest user) {
        UserSigninResponse response = new UserSigninResponse();

        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        } catch (BadCredentialsException e) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "User Not Authenticated");
            // throw new UsernameNotFoundException("User not found");
            return new ResponseEntity<Object>(map, HttpStatus.FORBIDDEN);
        }
        UserDto userDto = service.getUserByEmail(user.getEmail());
        if (!userDto.isEnabled()) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "User Not verified");

            return new ResponseEntity<Object>(map, HttpStatus.FORBIDDEN);
            // throw new UsernameNotFoundException("User not found");
        }
        String token = jwt.generateToken(user.getEmail());
        response.setUserId(userDto.getUserId());
        response.setToken(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/signup/verify/{verificationToken}")
    public ResponseEntity<String> verify(@PathVariable("verificationToken") String verificationToken) {

        try {
            service.verify(verificationToken);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unable to verify user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok("User Verified Succesfully");
    }

}
