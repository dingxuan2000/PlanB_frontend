package com.example.controller;

public class ticketInfo {
    String name,time,major,course;
    int image;

    public ticketInfo(String name,String major, String course, String time) {
        this.name = name;
        this.major = major;
        this.course = course;
        this.time = time;
        //this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
//    public int getImage() {
//        return image;
//    }
//
//    public void setImage(int image) {
//        this.image = image;
//    }
}
