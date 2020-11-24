package com.example.planb_frontend;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planb_backend.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TutorPageActivity extends AppCompatActivity {
    private ImageView connectionB;
    private ImageView historyB;
    private ImageView profileB;
    ListView listView;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    ListView lst;
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> time = new ArrayList<String>();
    ArrayList<String> major = new ArrayList<String>();
    ArrayList<String> course = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CustomListView customListView=new CustomListView(this, names, time, major, course);
        fStore.collection("student_ticket").whereEqualTo("status", "submitted").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        DocumentReference docref = fStore.collection("users").document(doc.getId());
                        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot docc = task.getResult();
                                    if (docc.exists()){
                                        names.add(docc.getString("preferred_name"));
                                        major.add(docc.getString("major"));
                                        time.add(doc.get("time_preference").toString());
                                        course.add(doc.get("course_code").toString());
                                        lst.setAdapter(customListView);
                                        Log.d("Document",docc.getString("preferred_name")+" "+doc.get("course_code").toString());
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tutor);



        lst=(ListView)findViewById(R.id.listview);
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