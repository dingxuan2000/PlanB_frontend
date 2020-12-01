package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planb_backend.User;

public class StudentHistoryActivity extends AppCompatActivity {

    private ImageView mainB;
    private ImageView connectionB;
    private ImageView profileB;
    //private User user;

    ListView lst;
    String[] names={"Caiwei Zhao", "Xuan Ding"};
    String[] time={"2020-05-26 09:10:00", "2020-05-29 09:30:00"};
    String[] course={"cse110", "maht183"};
    Integer[] tutor_imgid = {R.drawable.student3,R.drawable.student4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_student);

//        Intent prevIntent = getIntent();
//        user = (User) prevIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);

        lst=(ListView)findViewById(R.id.listview);
        StudentHistoryCustomListView customListView=new StudentHistoryCustomListView(this, names, time, course, tutor_imgid);
        lst.setAdapter(customListView);

        mainB = findViewById(R.id.student_main_btn);
        mainB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHistoryActivity.this, StudentPageActivity.class);
                //intent.putExtra(StudentRegisterActivity.GET_USER_KEY,user);
                startActivity(intent);
            }
        });

        connectionB = findViewById(R.id.student_connection_btn);
        connectionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHistoryActivity.this, StudentConnectionActivity.class);
                //intent.putExtra(StudentRegisterActivity.GET_USER_KEY,user);
                startActivity(intent);
            }
        });

        profileB = findViewById(R.id.student_profile_btn);
        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentHistoryActivity.this, StudentProfileActivity.class);
                //intent.putExtra(StudentRegisterActivity.GET_USER_KEY,user);
                startActivity(intent);
            }
        });


    }
}



