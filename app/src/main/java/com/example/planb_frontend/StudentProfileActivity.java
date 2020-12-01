package com.example.planb_frontend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planb_backend.User;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class StudentProfileActivity extends AppCompatActivity {
    private ImageView connectionB;
    private ImageView historyB;
    private ImageView mainB;
    private ImageView logout;
    private User user;


    private TextView studentName;
    private TextView gradeStanding;
    private TextView major;
    private TextView phoneNumber;
    private TextView email;

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_student);

        Intent prevIntent = getIntent();
        user = (User) prevIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);


        fAuth = FirebaseAuth.getInstance();

        studentName = findViewById(R.id.student_name);
        studentName.setText(user.getPreferred_name());
        gradeStanding = findViewById(R.id.grade_standing);
        gradeStanding.setText(user.getClass_standing());
        major = findViewById(R.id.major);
        major.setText(user.getMajor());
        phoneNumber = findViewById(R.id.phone_number);
        phoneNumber.setText(user.getPhone_number());
        email = findViewById(R.id.email);
        email.setText(user.getEmail());

        connectionB = findViewById(R.id.student_connection_btn);
        connectionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentProfileActivity.this, StudentConnectionActivity.class);
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.student_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentProfileActivity.this, StudentHistoryActivity.class);
                startActivity(intent);
            }
        });

        mainB = findViewById(R.id.student_main_btn);
        mainB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentProfileActivity.this, StudentPageActivity.class);
                startActivity(intent);
            }
        });

        //Logout verify box
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentProfileActivity.this);
                builder.setIcon(R.drawable.warning);
                builder.setTitle("Sign Out");
                builder.setMessage("Are you sure that you want to sign out?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        fAuth.signOut();
                        Intent intent = new Intent(StudentProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Successfully signed out", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Toast.makeText(StudentProfileActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //Logout verify box
//        logout = findViewById(R.id.logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(StudentProfileActivity.this);
////                builder.setIcon(R.drawable.warning);
//                builder.setTitle("Sign Out");
//                builder.setMessage("Are you sure that you want to sign out?");
//
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which){
//                        fAuth.signOut();
//                        Intent intent = new Intent(StudentProfileActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        Toast.makeText(getApplicationContext(), "Successfully signed out", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                });
//
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which){
//                        Toast.makeText(StudentProfileActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });
    }
}





