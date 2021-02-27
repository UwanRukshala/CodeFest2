package com.codefestfinal.codefest21.Model;

public class User {

    String firstName;
    String lastName;
    String nic;
    String mobile;
    String email;
    String gender;
    String imageURL;
    String fcmToken;

    public User() {
    }

    public User(String firstName, String lastName, String nic, String mobile, String email, String gender, String imageURL, String fcmToken) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nic = nic;
        this.mobile = mobile;
        this.email = email;
        this.gender = gender;
        this.imageURL = imageURL;
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
