package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseActivity extends AppCompatActivity {

    private ImageButton studentChooseBtn;
    private ImageButton tutorChooseBtn;
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
    }
}


