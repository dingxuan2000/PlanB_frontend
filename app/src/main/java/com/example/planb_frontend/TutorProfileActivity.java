package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planb_backend.User;

public class TutorProfileActivity extends AppCompatActivity {
    private ImageView connectionB;
    private ImageView historyB;
    private ImageView mainB;
    private ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_tutor);

        Intent intent = getIntent();
        User passUser = (User) intent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);
        Toast.makeText(getApplicationContext(), passUser.getId(), Toast.LENGTH_SHORT).show();

        connectionB = findViewById(R.id.tutor_connection_btn);
        connectionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorProfileActivity.this, TutorConnectionActivity.class);
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.tutor_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorProfileActivity.this, TutorHistoryActivity.class);
                startActivity(intent);
            }
        });

        mainB = findViewById(R.id.tutor_main_btn);
        mainB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorProfileActivity.this, TutorPageActivity.class);
                startActivity(intent);
            }
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}







