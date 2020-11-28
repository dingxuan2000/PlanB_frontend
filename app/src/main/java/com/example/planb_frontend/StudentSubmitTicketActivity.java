package com.example.planb_frontend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentSubmitTicketActivity extends AppCompatActivity {

    private TextView btn;
    Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_ticket);

        //initialize button
        btn = findViewById(R.id.Online_Tutoring);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //myButton.setBackgroundColor(R.color.black);
                btn.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        });


    }
}





