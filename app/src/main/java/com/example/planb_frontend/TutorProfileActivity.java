package com.example.planb_frontend;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_tutor);

        fAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        User passUser = (User) intent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);
        Toast.makeText(getApplicationContext(), "AT PROFILE"+passUser.toString(), Toast.LENGTH_SHORT).show();

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
                Intent previousIntent = getIntent();
                User addCourseUser = (User)previousIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);
                Toast.makeText(getApplicationContext(),"LEAVING PROFILE: "+addCourseUser.toString(),Toast.LENGTH_LONG).show();
                Intent addCourse = new Intent(TutorProfileActivity.this,AddCourses.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,addCourseUser);
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
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,passUser);
                startActivity(intent);
            }
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TutorProfileActivity.this);
                builder.setIcon(R.drawable.warning);
                builder.setTitle("Sign Out");
                builder.setMessage("Are you sure that you want to sign out?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        fAuth.signOut();
                        Intent intent = new Intent(TutorProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Successfully signed out", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Toast.makeText(TutorProfileActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}







