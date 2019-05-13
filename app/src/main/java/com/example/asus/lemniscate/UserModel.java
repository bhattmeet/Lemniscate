package com.example.asus.lemniscate;


public class UserModel {
    private String id;
    private String fullName;
    private String emailid;
    private String gener;
    private String radiobutton;
    private String website;
    private String number;
    private String gender;
    private String userimage;

    public UserModel(){

    }

    public UserModel(String id, String fullName, String emailid, String gener, String website, String number, String gender, String userimage,String radiobutton) {
        this.id = id;
        this.fullName = fullName;
        this.emailid = emailid;
        this.gener = gener;
        this.website = website;
        this.number = number;
        this.gender = gender;
        this.userimage = userimage;
        this.radiobutton = radiobutton;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getRadiobutton() {
        return radiobutton;
    }

    public void setRadiobutton(String radiobutton) {
        this.radiobutton = radiobutton;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGener() {
        return gener;
    }

    public void setGener(String gener) {
        this.gener = gener;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }


}
