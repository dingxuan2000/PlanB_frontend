package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planb_backend.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class TutorProfileActivity extends AppCompatActivity {
    private ImageView connectionB;
    private ImageView historyB;
    private ImageView mainB;
    private ImageView logout;
    private ImageView courseadd;

    private FirebaseAuth fAuth;

    private TextView studentName;
    private TextView gradeStanding;
    private TextView major;
    private TextView phoneNumber;
    private TextView email;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_tutor);

        fAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        User passUser = (User) intent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);
        Toast.makeText(getApplicationContext(), passUser.getId(), Toast.LENGTH_SHORT).show();

        studentName = findViewById(R.id.tutor_name);
        studentName.setText(passUser.getPreferred_name());
        gradeStanding = findViewById(R.id.grade_standing);
        gradeStanding.setText(passUser.getClass_standing());
        major = findViewById(R.id.major);
        major.setText(passUser.getMajor());
        phoneNumber = findViewById(R.id.phone_number);
        phoneNumber.setText(passUser.getPhone_number());
        email = findViewById(R.id.email);
        email.setText(passUser.getEmail());

        courseadd = findViewById(R.id.courseadd);
        courseadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addCourse = new Intent(getApplicationContext(),CourseActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,user);
                startActivity(addCourse);
            }
        });

        connectionB = findViewById(R.id.tutor_connection_btn);
        connectionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorProfileActivity.this, TutorConnectionActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,passUser);
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.tutor_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorProfileActivity.this, TutorHistoryActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,passUser);
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
                fAuth.signOut();
                Intent intent = new Intent(TutorProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Successfully signed out", Toast.LENGTH_SHORT).show();
            }
        });
    }
}







