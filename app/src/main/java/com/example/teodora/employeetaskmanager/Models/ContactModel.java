package com.example.teodora.employeetaskmanager.Models;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class ContactModel {

    private String Address;
    private String Email;
    private String Image;
    private String MobilePhone;
    private String Name;
    @Exclude
    public String Contacts;

    public ContactModel(String address, String email, String image, String mobilePhone, String name) {
        Address = address;
        Email = email;
        Image = image;
        MobilePhone = mobilePhone;
        Name = name;
    }

    public ContactModel (String name, String email, String mobilePhone){
        Email = email;
        MobilePhone = mobilePhone;
        Name = name;
    }



    public ContactModel() {
    }


    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getMobilePhone() {
        return MobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        MobilePhone = mobilePhone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
