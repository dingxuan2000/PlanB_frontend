package com.example.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import com.example.model.User;


public class TutorHistoryActivity extends AppCompatActivity {

    private ImageView mainB;
    private ImageView connectionB;
    private ImageView profileB;

    ListView lst;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    User user;
    private static final String TAG = "TutorHistory";
    String student_name = "";
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> meeting_time = new ArrayList<String>();
    ArrayList<String> course_id = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        User passUser = (User) intent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);

        TutorHistoryCustomListView customListView=new TutorHistoryCustomListView(this, name, meeting_time, course_id);

        fAuth = FirebaseAuth.getInstance();
        CollectionReference usersRef = fStore.collection("users");
        if (fAuth.getCurrentUser() != null) {
            String userId = fAuth.getCurrentUser().getUid();

            fStore.collection("history_meetings")
                    .whereEqualTo("tutor_id", userId)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots,
                                            @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }

                            if (snapshots.isEmpty()) {
                                Log.w(TAG, "no docs");
                            } else {
                                for (DocumentSnapshot doc: snapshots.getDocuments()) {
                                    if (doc.exists()) {
                                        usersRef.whereEqualTo("user id", doc.getString("student_id"))
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        student_name = document.getData().get("preferred_name").toString();
                                                        name.add(student_name);
                                                        meeting_time.add(doc.getString("time_period"));
                                                        course_id.add(doc.getString("course"));
                                                        lst.setAdapter(customListView);
                                                    }
                                                } else { Log.d(TAG, "error getting student name", task.getException());}
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "current user null", Toast.LENGTH_LONG).show();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_tutor);

        lst=(ListView)findViewById(R.id.listview);

        mainB = findViewById(R.id.student_main_btn);
        mainB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorHistoryActivity.this, TutorPageActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,passUser);
                startActivity(intent);
            }
        });

        connectionB = findViewById(R.id.student_connection_btn);
        connectionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorHistoryActivity.this, TutorConnectionActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,passUser);
                startActivity(intent);
            }
        });

        profileB = findViewById(R.id.student_profile_btn);
        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorHistoryActivity.this, TutorProfileActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,passUser);
                startActivity(intent);
            }
        });



    }
}


