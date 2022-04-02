package com.project.contacts.contacts.Services;

import com.project.contacts.contacts.Models.Dto.UserDto;

import org.springframework.stereotype.Service;

@Service
public interface UserServices {
    public UserDto signupUser(UserDto user);
}
