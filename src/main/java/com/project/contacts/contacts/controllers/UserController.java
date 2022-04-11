package com.project.contacts.contacts.controllers;

import javax.servlet.http.HttpServletRequest;

import com.project.contacts.contacts.Models.ResponseMessageModel;
import com.project.contacts.contacts.Models.UserProfileModel;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            ResponseMessageModel msg = new ResponseMessageModel();

            msg.setMessage("Unable to send Mail, Please try again later");

            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        res.setMessage("Email Sent Successfully!!!,Please check your mail to verify");
        // System.out.println(userDto.getEncryptedPassword());
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
            ResponseMessageModel msg = new ResponseMessageModel();

            msg.setMessage("User Not Authenticated");
            // throw new UsernameNotFoundException("User not found");
            return new ResponseEntity<Object>(msg, HttpStatus.FORBIDDEN);
        }
        UserDto userDto = service.getUserByEmail(user.getEmail());
        if (!userDto.isEnabled()) {
            ResponseMessageModel msg = new ResponseMessageModel();

            msg.setMessage("User Not verified");

            return new ResponseEntity<Object>(msg, HttpStatus.FORBIDDEN);
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

    @PutMapping("/updateProfile")
    public ResponseEntity<Object> updateProfile(@RequestBody UserProfileModel user) {
        // name image phoneno
        UserDto userDto = null;
        try {
            userDto = service.getUserByEmail(user.getEmail());
        } catch (Exception e) {
            ResponseMessageModel msg = new ResponseMessageModel();
            msg.setMessage(e.toString());
            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (userDto.isEnabled() == false) {
            ResponseMessageModel msg = new ResponseMessageModel();
            msg.setMessage("User Not Authenticated");

            // throw new UsernameNotFoundException("User not found");
            return new ResponseEntity<Object>(msg, HttpStatus.FORBIDDEN);
        }

        ResponseMessageModel msg = new ResponseMessageModel();
        boolean flag = service.updateUserProfile(userDto, user);
        if (!flag) {
            msg.setMessage("Unable to update your Profile. Please try again later.");

            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        msg.setMessage("Profile Updated Successfully!!");
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/getUser")
    public ResponseEntity<Object> getUser(@RequestParam String userId) {
        UserProfileModel res = new UserProfileModel();
        try {
            UserDto userDto = service.getUserById(userId);
            BeanUtils.copyProperties(userDto, res);

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            ResponseMessageModel msg = new ResponseMessageModel();
            msg.setMessage(e.getMessage());
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
    }

}
