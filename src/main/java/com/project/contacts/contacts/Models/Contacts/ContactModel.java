package com.project.contacts.contacts.Models.Contacts;

import lombok.Data;

@Data
public class ContactModel {
    private String contactId;
    private String name;
    private String phoneNo;
    private String email;
    private String image;
    private String category;
    private String description;

    public ContactModel() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContactModel(String contactId, String name, String phoneNo, String email, String image, String category,
            String desc) {
        this.contactId = contactId;
        this.category = category;
        this.name = name;
        this.phoneNo = phoneNo;
        this.email = email;
        this.image = image;
        this.description = desc;
    }

    public String getName() {
        return name;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
