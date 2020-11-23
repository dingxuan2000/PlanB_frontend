package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class TutorHistoryActivity extends AppCompatActivity {

    private ImageView mainB;
    private ImageView connectionB;
    private ImageView profileB;

    ListView lst;
    String[] names={"Zanyuan Yang", "Baitong"};
    String[] time={"2020-05-26 09:10:00", "2020-05-29 09:30:00"};
    String[] course={"cse110", "maht183"};
    Integer[] tutor_imgid = {R.drawable.student1,R.drawable.student2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_tutor);

        lst=(ListView)findViewById(R.id.listview);
        TutorHistoryCustomListView customListView=new TutorHistoryCustomListView(this, names, time, course, tutor_imgid);
        lst.setAdapter(customListView);

        mainB = findViewById(R.id.tutor_main_btn);
        mainB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorHistoryActivity.this, TutorPageActivity.class);
                startActivity(intent);
            }
        });

        connectionB = findViewById(R.id.tutor_connection_btn);
        connectionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorHistoryActivity.this, TutorConnectionActivity.class);
                startActivity(intent);
            }
        });

        profileB = findViewById(R.id.tutor_profile_btn);
        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorHistoryActivity.this, TutorProfileActivity.class);
                startActivity(intent);
            }
        });



    }
}


