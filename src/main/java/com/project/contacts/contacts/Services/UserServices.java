package com.project.contacts.contacts.Services;

import com.project.contacts.contacts.Models.Dto.UserDto;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServices extends UserDetailsService

{
    public UserDto signupUser(UserDto user);

    public UserDto getUserByEmail(String email);
}
