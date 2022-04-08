package com.project.contacts.contacts.controllers;

import java.util.*;

import javax.servlet.http.HttpSession;
import com.project.contacts.contacts.Models.Dto.UserDto;
import com.project.contacts.contacts.Models.SigninModels.UserSigninRequest;
import com.project.contacts.contacts.Services.EmailService;
import com.project.contacts.contacts.Services.OtpService;
import com.project.contacts.contacts.Services.UserServices;
import com.project.contacts.contacts.Utilities.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password")
public class PasswordController {

    @Autowired
    UserServices userServices;
    @Autowired
    EmailService emailService;
    @Autowired
    Utils utils;
    @Autowired
    OtpService otpService;

    @GetMapping("/forgotPassword")
    public ResponseEntity<Object> forgotPassword(@RequestParam String email) {
        // verify email in db
        UserDto userDto = null;
        Map<String, String> map = new HashMap<>();
        try {
            userDto = userServices.getUserByEmail(email);
            if (userDto.isEnabled() == false)
                throw new Exception();
        } catch (Exception e) {
            // userDto = null;
            map.put("message", "User Not Found, Enter a valid email.");
            System.out.println(e);

            return new ResponseEntity<Object>(map, HttpStatus.FORBIDDEN);
        }

        // if exist send otp
        String otp = "" + otpService.generateOTP(userDto.getEmail());
        System.out.println("Generated OTP " + otp);
        boolean flag = emailService.sendOtp(email, otp);
        // sendOtp();
        if (!flag) {
            map.put("message", "Unable to Send OTP. Please Try again later");
            otpService.clearOTP(userDto.getEmail());
            return new ResponseEntity<Object>(map, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        map.put("message", "OTP Sent Succesfully. Check your email for the OTP.");

        return new ResponseEntity<Object>(map, HttpStatus.OK);
        // else no user found message
        // store otp

    }

    @GetMapping("/verifyOTP")
    public ResponseEntity<Object> verifyOtp(@RequestParam String enteredOtp, @RequestParam String email) {

        String originalOtp = "" + otpService.getOtp(email);
        Map<String, Boolean> map = new HashMap<>();
        System.out.println("session OTP " + originalOtp);
        System.out.println("entered OTP " + enteredOtp);

        if (originalOtp.equals(enteredOtp)) {
            map.put("verified", true);
            otpService.setOtp(email);
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        } else {
            map.put("verified", false);

            return new ResponseEntity<Object>(map, HttpStatus.FORBIDDEN);

        }

    }

    @PostMapping("/changePassword")
    public ResponseEntity<Object> changePassword(@RequestBody UserSigninRequest req) {
        Map<String, String> map = new HashMap<>();
        try {
            userServices.changePassword(req.getEmail(), req.getPassword());
            map.put("message", "Password Changed Successfully!!!");
        } catch (Exception e) {
            map.put("message", "Password Changed Unsuccessfully!!!");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);

        }
        return new ResponseEntity<>(map, HttpStatus.OK);
        // return true;

    }

}
