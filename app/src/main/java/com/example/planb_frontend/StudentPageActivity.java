package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentPageActivity extends AppCompatActivity {

    private ImageView connectionB;
    private ImageView historyB;
    private ImageView profileB;
    private ImageView submit_ticketB;

    //create custom adapter
    ListView tutor_rank;
    String[] tutor_name = {"caiwei zhao","zanyuan yang","zihao chen"};
    String[] tutor_major = {"cs","cs","cs"};
    String[] tutor_grade = {"senior","senior","senior"};
    Integer[] tutor_imgid = {R.drawable.tutor_img1,R.drawable.tutor_img2,R.drawable.tutor_img3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_student);

        tutor_rank = findViewById(R.id.tutor_listview);
        studentCustomListView clv = new studentCustomListView(this,tutor_name,tutor_major,tutor_grade,tutor_imgid);
        tutor_rank.setAdapter(clv);

        connectionB = findViewById(R.id.student_connection_btn);
        connectionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentPageActivity.this, StudentConnectionActivity.class);
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.student_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentPageActivity.this, StudentHistoryActivity.class);
                startActivity(intent);
            }
        });

        profileB = findViewById(R.id.student_profile_btn);
        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentPageActivity.this, StudentProfileActivity.class);
                startActivity(intent);
            }
        });

        submit_ticketB = findViewById(R.id.student_submit_ticket);
        submit_ticketB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentPageActivity.this, SubmitTicketActivity.class);
                startActivity(intent);
            }
        });

    }
}
