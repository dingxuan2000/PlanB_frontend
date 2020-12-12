package com.example.controller;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.model.User;

public class WelcomePageActivity extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run(){
//                Intent mainToStart = new Intent(getApplicationContext(), StudentPageActivity.class);
//                WelcomePageActivity.this.startActivity(mainToStart);
//                WelcomePageActivity.this.finish();
//
//
//
//            }
//        }, 2500);
    }

}
