package com.example.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String email;

    private String preferred_name;

    private String major;

    private String class_standing;

    private String phone_number;

    private String id;

    private String type;

    private Integer rating;

    public User() {
    }

    public User(String email, String preferred_name, String major, String class_standing, String phone_number, String id, String type, Integer rating) {
        this.email = email;
        this.preferred_name = preferred_name;
        this.major = major;
        this.class_standing = class_standing;
        this.phone_number = phone_number;
        this.id = id;
        this.type = type;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreferred_name() {
        return preferred_name;
    }

    public void setPreferred_name(String preferred_name) {
        this.preferred_name = preferred_name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClass_standing() {
        return class_standing;
    }

    public void setClass_standing(String class_standing) {
        this.class_standing = class_standing;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", preferred_name='" + preferred_name + '\'' +
                ", major='" + major + '\'' +
                ", class_standing='" + class_standing + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
