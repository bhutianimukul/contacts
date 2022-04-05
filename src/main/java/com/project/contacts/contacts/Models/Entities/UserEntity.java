package com.project.contacts.contacts.Models.Entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.*;

@Entity(name = "users")
public class UserEntity implements Serializable {
    public static final long serialVersionUID = 10l;
    @Id
    @Column(name = "user_id", length = 30)
    private String userId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "phone_no", nullable = false, length = 10)
    private String phoneNo;
    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContactEntity> contacts;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
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

    public List<ContactEntity> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactEntity> contacts) {
        this.contacts = contacts;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

}
