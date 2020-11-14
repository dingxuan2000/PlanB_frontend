package com.example.planb_frontend;

import com.google.gson.annotations.SerializedName;

public class Post {
    private String username;
    private int password;

    @SerializedName("body")
    private String text;

    public String getUsername() {
        return username;
    }

    public int getPassword() {
        return password;
    }

    public String getText() {
        return text;
    }
}
