package com.mahdirahmani8.Model;

import java.util.ArrayList;
import java.util.Date;

public class User {

    // vars
    private int id ;
    private String name;
    private String email;
    private String password;
    private String bio;
    private String tell;
    private String site;
    private boolean isVerified, isForgotPass;
    private Date date;
    private ArrayList<Integer> follow_category;
    private String imgPro;

    // getter and setter


    public boolean isForgotPass() {
        return isForgotPass;
    }

    public void setForgotPass(boolean forgotPass) {
        isForgotPass = forgotPass;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public boolean isLogin() {
        return isVerified;
    }

    public void setLogin(boolean login) {
        isVerified = login;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Integer> getFollowCategory() {
        return follow_category;
    }

    public void setFollowCategory(ArrayList<Integer> followCategory) {
        this.follow_category = followCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public ArrayList<Integer> getFollow_category() {
        return follow_category;
    }

    public void setFollow_category(ArrayList<Integer> follow_category) {
        this.follow_category = follow_category;
    }

    public String getImgPro() {
        return imgPro;
    }

    public void setImgPro (String imgPro) {
        this.imgPro = imgPro;
    }
}
