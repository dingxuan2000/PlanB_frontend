package com.example.planb_frontend;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TutorPageActivity extends AppCompatActivity {
    private ImageView connectionB;
    private ImageView historyB;
    private ImageView profileB;

    ListView lst;
    String[] names={"Zihao Chen", "Caiwei Zhao", "Kristina", "Kiwi"};
    String[] time={"9:10", "10:30", "15:30", "15:35"};
    String[] major={"computer", "math", "physic", "music"};
    String[] course={"cse110", "maht183", "phy11", "vis116"};
    Integer[] tutor_imgid = {R.drawable.student1,R.drawable.student2,R.drawable.student3, R.drawable.student4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tutor);

        lst=(ListView)findViewById(R.id.listview);
        CustomListView customListView=new CustomListView(this, names, time, major, course, tutor_imgid);
        lst.setAdapter(customListView);


        connectionB = findViewById(R.id.tutor_connection_btn);
        connectionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorPageActivity.this, TutorConnectionActivity.class);
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.tutor_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorPageActivity.this, TutorHistoryActivity.class);
                startActivity(intent);
            }
        });

        profileB = findViewById(R.id.tutor_profile_btn);
        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorPageActivity.this, TutorProfileActivity.class);
                startActivity(intent);
            }
        });



    }
}