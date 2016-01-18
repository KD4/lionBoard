package com.github.lionboard.model;

/**
 * Created by daum on 16. 1. 17..
 */
public class User {
    private int id;
    private String identity;
    private String email;
    private String name;
    private String password;
    private String profileUrl;
    private int userStateCode;
    private int powerCode;

    public User(){

    }
    public User(String identity, String email,String name, String password, String profileUrl, int userStateCode, int powerCode){
        this.identity = identity;
        this.email = email;
        this.name = name;
        this.password = password;
        this.profileUrl = profileUrl;
        this.userStateCode = userStateCode;
        this.powerCode = powerCode;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public int getUserStateCode() {
        return userStateCode;
    }

    public void setUserStateCode(int userStateCode) {
        this.userStateCode = userStateCode;
    }

    public int getPowerCode() {
        return powerCode;
    }

    public void setPowerCode(int powerCode) {
        this.powerCode = powerCode;
    }
}
