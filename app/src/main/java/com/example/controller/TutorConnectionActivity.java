package com.example.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TutorConnectionActivity extends AppCompatActivity {

    private ImageView mainB;
    private ImageView historyB;
    private ImageView profileB;
    private LinearLayout meet;
    private LinearLayout upcoming;


    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    private ListView UpcomingList;
    private ListView OngoingList;

    private ArrayList<String> upcoming_student_name= new ArrayList<String>();
    private ArrayList<String> upcoming_course_code= new ArrayList<String>();
    private ArrayList<String> upcoming_time= new ArrayList<String>();
    private ArrayList<String> upcoming_student_phone= new ArrayList<String>();
    private ArrayList<String> upcoming_meeting_id = new ArrayList<String>();

    private ArrayList<String> ongoing_student_name= new ArrayList<String>();
    private ArrayList<String> ongoing_course_code= new ArrayList<String>();
    private ArrayList<String> ongoing_time= new ArrayList<String>();
    private ArrayList<String> ongoing_student_phone= new ArrayList<String>();
    private ArrayList<String> ongoing_meeting_id = new ArrayList<String>();
    private ArrayList<String> ongoing_student_id = new ArrayList<String>();

    private String tutor_id;
    private String student_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_tutor);

        Intent intent = getIntent();
        //This user is the current Tutor
        User passUser = (User) intent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);
        tutor_id = passUser.getId();

        //Check
        Query queryAll = fStore.collection(AcceptTicketActivity.MEETING_TABLE_KEY).whereEqualTo(AcceptTicketActivity.TUTOR_ID_KEY, tutor_id);
        Query queryUpcoming;
        Query queryOngoing;

        if(queryAll != null) {
            queryUpcoming = queryAll.whereEqualTo(AcceptTicketActivity.STATUS_KEY, AcceptTicketActivity.meetingUninitiated);
            queryOngoing = queryAll.whereEqualTo(AcceptTicketActivity.STATUS_KEY, AcceptTicketActivity.meetingOngoing);

            if(queryUpcoming!=null){
                TutorUpcomingCustomListView TCListView = new TutorUpcomingCustomListView(this, upcoming_student_name, upcoming_course_code, upcoming_time, upcoming_student_phone, upcoming_meeting_id, passUser);
                queryUpcoming.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                student_id = doc.get("student_id").toString();
                                DocumentReference docref = fStore.collection("users").document(student_id);
                                docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot docc = task.getResult();
                                            if (docc.exists()){
                                                upcoming_meeting_id.add(doc.getId());
                                                upcoming_student_name.add(docc.getString(StudentRegisterActivity.PREFERRED_NAME_KEY));
                                                upcoming_student_phone.add(docc.getString(StudentRegisterActivity.PHONE_NUMBER_KEY));
                                                upcoming_course_code.add(doc.getString(AcceptTicketActivity.COURSE_KEY));
                                                upcoming_time.add(doc.getString(AcceptTicketActivity.TIME_PERIOD_KEY));
                                                UpcomingList.setAdapter(TCListView);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

                //set the screen
                UpcomingList = (ListView) findViewById(R.id.tutor_upcoming_listview);
                UpcomingList.setAdapter(TCListView);

            }
            else{
                Toast.makeText(getApplicationContext(), "No Upcoming Meeting", Toast.LENGTH_SHORT).show();
            }

            if(queryOngoing!=null){
                // To be Implemented
                //Use setText to set fields
                TutorOngoingCustomListView TOCustomListView = new TutorOngoingCustomListView(this, ongoing_student_name, ongoing_course_code, ongoing_time, ongoing_student_phone, ongoing_meeting_id, ongoing_student_id, passUser);
                queryOngoing.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                student_id = doc.get("student_id").toString();
                                DocumentReference docref = fStore.collection("users").document(student_id);
                                docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot docc = task.getResult();
                                            if (docc.exists()){
                                                ongoing_student_id.add(docc.getId());
                                                ongoing_meeting_id.add(doc.getId());
                                                ongoing_student_name.add(docc.getString(StudentRegisterActivity.PREFERRED_NAME_KEY));
                                                ongoing_student_phone.add(docc.getString(StudentRegisterActivity.PHONE_NUMBER_KEY));
                                                ongoing_course_code.add(doc.getString(AcceptTicketActivity.COURSE_KEY));
                                                ongoing_time.add(doc.getString(AcceptTicketActivity.TIME_PERIOD_KEY));
                                                OngoingList.setAdapter(TOCustomListView);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

                OngoingList = (ListView) findViewById(R.id.tutor_ongoing_listview);
                OngoingList.setAdapter(TOCustomListView);
            }
            else{
                Toast.makeText(getApplicationContext(), "No Ongoing Meeting", Toast.LENGTH_SHORT).show();
            }


        }
        else{
            Toast.makeText(getApplicationContext(), "No Available Meeting", Toast.LENGTH_SHORT).show();
        }




        mainB = findViewById(R.id.tutor_main_btn);
        mainB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorConnectionActivity.this, TutorPageActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,passUser);
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.tutor_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorConnectionActivity.this, TutorHistoryActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,passUser);
                startActivity(intent);
            }
        });

        profileB = findViewById(R.id.tutor_profile_btn);
        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorConnectionActivity.this, TutorProfileActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,passUser);
                startActivity(intent);
            }
        });


    }
}

