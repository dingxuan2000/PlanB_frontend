package com.example.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseActivity extends AppCompatActivity {

    private ImageButton studentChooseBtn;
    private ImageButton tutorChooseBtn;
    private Button chooseLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);

        studentChooseBtn = findViewById(R.id.studentBtn);
        studentChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseActivity.this, StudentRegisterActivity.class);
                startActivity(intent);
            }
        });

        tutorChooseBtn = findViewById(R.id.tutorBtn);
        tutorChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseActivity.this, TutorRegisterActivity.class);
                startActivity(intent);
            }
        });

        chooseLogin = findViewById(R.id.choose_login);
        chooseLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}


