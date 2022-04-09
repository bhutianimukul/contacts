package com.project.contacts.contacts.Services;

import com.project.contacts.contacts.Models.Dto.UserDto;
import com.project.contacts.contacts.Models.SignupModels.UserSignupRequest;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServices extends UserDetailsService

{
    public UserDto signupUser(UserDto user);

    public UserDto getUserByEmail(String email);

    public void removeUser(UserDto user) throws Exception;

    public void verify(String verificationToken) throws Exception;

    public boolean changePassword(String email, String newPassword);

    public boolean updateUserProfile(UserDto userDto, UserSignupRequest user);
}
