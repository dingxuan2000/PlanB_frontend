package com.example.controller;

import javax.annotation.Nullable;

public class tutorCourse {
    String uid,course_1,course_2,course_3,course_4;

    public tutorCourse(String uid, @Nullable String course_1, @Nullable String course_2, @Nullable String course_3,
                       @Nullable String course_4) {
        this.uid = uid;

        this.course_1 = course_1;
        this.course_2 = course_2;
        this.course_3 = course_3;
        this.course_4 = course_4;

        //this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCourse_1() {
        return course_1;
    }

    public void setCourse_1(String course_1) {
        this.course_1 = course_1;
    }

    public String getCourse_2() {
        return course_2;
    }

    public void setCourse_2(String course_2) {
        this.course_2 = course_2;
    }

    public String getCourse_3() {
        return course_3;
    }

    public void setCourse_3(String course_3) {
        this.course_3 = course_3;
    }

    public String getCourse_4() {
        return course_4;
    }

    public void setCourse_4(String course_4) {
        this.course_4 = course_4;
    }


//    public int getImage() {
//        return image;
//    }
//
//    public void setImage(int image) {
//        this.image = image;
//    }
}
