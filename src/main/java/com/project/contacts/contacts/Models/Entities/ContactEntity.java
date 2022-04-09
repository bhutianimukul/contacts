package com.project.contacts.contacts.Models.Entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "contacts")
public class ContactEntity implements Serializable {
    public static final long serialVersionUID = 10l;
    @Id
    @Column(name = "contact_id", length = 30)
    private String contactId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "phone_no", nullable = false, length = 10)
    private String phoneNo;
    @Column(name = "image")
    private String image;

    @Column(name = "category", nullable = false)
    private String category;
    @Column(name = "description", nullable = false, length = 200)
    private String description;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public UserEntity user;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String userId) {
        this.contactId = userId;
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

    public String getCategory() {
        return category;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}