package com.project.contacts.contacts.Models.Dto;

public class UserDto {
    private String name;
private String email;
private String phoneNo;
private String EncryptedPassword;
private String password;
public String getName() {
    return name;
}
public String getPassword() {
    return password;
}
public void setPassword(String password) {
    this.password = password;
}
public void setName(String name) {
    this.name = name;
}
public String getEmail() {
    return email;
}
public void setEmail(String email) {
    this.email = email;
}
public String getPhoneNo() {
    return phoneNo;
}
public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
}
public String getEncryptedPassword() {
    return EncryptedPassword;
}
public void setEncryptedPassword(String encryptedPassword) {
    EncryptedPassword = encryptedPassword;
}
public String getUserId() {
    return userId;
}
public void setUserId(String userId) {
    this.userId = userId;
}
private String userId;
}
