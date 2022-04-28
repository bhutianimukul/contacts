package com.project.contacts.contacts.controllers;

import com.project.contacts.contacts.Models.ResponseMessageModel;
import com.project.contacts.contacts.Models.Dto.UserDto;
import com.project.contacts.contacts.Models.SigninModels.UserSigninRequest;
import com.project.contacts.contacts.Services.EmailService;
import com.project.contacts.contacts.Services.OtpService;
import com.project.contacts.contacts.Services.UserServices;
import com.project.contacts.contacts.Utilities.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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

    public ResponseEntity<ResponseMessageModel> forgotPassword(@RequestParam String email) {
        // verify email in db
        UserDto userDto = null;
        ResponseMessageModel msg = new ResponseMessageModel();
        try {
            userDto = userServices.getUserByEmail(email);
            if (userDto.isEnabled() == false)
                throw new Exception();
        } catch (Exception e) {
            // userDto = null;
            msg.setMessage("User Not Found, Enter a valid email.");

            // System.out.println(e);

            return new ResponseEntity<ResponseMessageModel>(msg, HttpStatus.FORBIDDEN);
        }

        // if exist send otp
        String otp = "" + otpService.generateOTP(userDto.getEmail());
        // System.out.println("Generated OTP " + otp);
        boolean flag = emailService.sendOtp(email, otp);
        // sendOtp();
        if (!flag) {
            msg.setMessage("Unable to Send OTP. Please Try again later");

            otpService.clearOTP(userDto.getEmail());
            return new ResponseEntity<ResponseMessageModel>(msg, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        msg.setMessage("OTP Sent Succesfully. Check your email for the OTP.");

        return new ResponseEntity<ResponseMessageModel>(msg, HttpStatus.OK);
        // else no user found message
        // store otp

    }

    @GetMapping("/verifyOTP")

    public ResponseEntity<ResponseMessageModel> verifyOtp(@RequestParam String enteredOtp, @RequestParam String email) {

        String originalOtp = "" + otpService.getOtp(email);
        ResponseMessageModel msg = new ResponseMessageModel();
        // System.out.println("session OTP " + originalOtp);
        // System.out.println("entered OTP " + enteredOtp);

        if (originalOtp.equals(enteredOtp)) {
            msg.setMessage("OTP VERIFIED");
            otpService.setOtp(email);
            return new ResponseEntity<ResponseMessageModel>(msg, HttpStatus.OK);
        } else {
            msg.setMessage("unable to verify otp");

            return new ResponseEntity<ResponseMessageModel>(msg, HttpStatus.FORBIDDEN);

        }

    }

    @PostMapping("/changePassword")

    public ResponseEntity<ResponseMessageModel> changePassword(@RequestBody UserSigninRequest req) {
        ResponseMessageModel msg = new ResponseMessageModel();

        try {
            userServices.changePassword(req.getEmail(), req.getPassword());
            msg.setMessage("Password Changed Successfully!!!");

        } catch (Exception e) {
            msg.setMessage("Unable to change password");

            return new ResponseEntity<>(msg, HttpStatus.FORBIDDEN);

        }
        return new ResponseEntity<>(msg, HttpStatus.OK);
        // return true;

    }

}
