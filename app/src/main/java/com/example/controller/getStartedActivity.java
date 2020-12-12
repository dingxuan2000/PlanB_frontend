package com.example.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class getStartedActivity extends AppCompatActivity {

    private Button getStartedBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_started);

        getStartedBtn = findViewById(R.id.Get_Started);
        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getStartedActivity.this, ChooseActivity.class);
                startActivity(intent);
            }
        });
    }
}
