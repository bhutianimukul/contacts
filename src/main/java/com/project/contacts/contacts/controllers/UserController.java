package com.project.contacts.contacts.controllers;

import com.project.contacts.contacts.Models.UserSignupRequest;
import com.project.contacts.contacts.Models.UserSignupResponse;
import com.project.contacts.contacts.Models.Dto.UserDto;
import com.project.contacts.contacts.Services.UserServices;
import com.project.contacts.contacts.Utilities.Utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@PostMapping("/signup")     
public UserSignupResponse userSignup(@RequestBody UserSignupRequest user){
UserSignupResponse res=new UserSignupResponse();
BeanUtils.copyProperties(user, res);
res.setUserId(util.generateUserId(30));
UserDto dtoSend=new UserDto();
BeanUtils.copyProperties(res, dtoSend);
BeanUtils.copyProperties(user, dtoSend);
 UserDto userDto=service.signupUser(dtoSend);
System.out.println(userDto.getEncryptedPassword());
return res;

}

@PostMapping("/signin")     
public String userSignin(){

return "hello login";

}


}
