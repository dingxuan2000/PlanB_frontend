package com.example.planb_frontend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class AcceptTicketActivity extends AppCompatActivity {


    //Meeting Object fields
    public static final String MEETING_ID_KEY = "meeting_id";
    public static final String COMMENT_KEY = "comment";
    public static final String TIME_PERIOD_KEY = "time_period";
    public static final String STATUS_KEY= "status";
    public static final String STUDENT_ID_KEY = "student_id";
    public static final String TUTOR_ID_KEY = "tutor_id";
    public static final String STUDENT_RATING_KEY = "student_rating";
    public static final String TUTOR_RATING_KEY = "tutor_rating";
    public static final String COURSE_KEY = "course";
    public static final String MEETING_TABLE_KEY = "meetings";


    private FirebaseFirestore fStore;

    //Meeting Status
    public static final String meetingUninitiated = "Uninitiated";
    public static final String meetingOngoing = "Ongoing";
    public static final String meetingCompleted = "Completed";


    private String student_id;
    private String tutor_id;
    private String student_name;
   // private String major;
    private String time;
    private String course;
    private String ticket_status;
    private String ticket_id;
    private String comment;
    private String meeting_id;


    //控件
    private ImageView backArrow;
    private Button mBtnAccept;
    //private




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_ticket);



        fStore = FirebaseFirestore.getInstance();
        mBtnAccept = findViewById(R.id.AcceptTicket);

        //get fields from TutorPageActivity
        Intent intent = getIntent();
        tutor_id = intent.getStringExtra(TUTOR_ID_KEY);
        student_id = intent.getStringExtra(STUDENT_ID_KEY);
        course = intent.getStringExtra(COURSE_KEY);
        time = intent.getStringExtra(TIME_PERIOD_KEY);
        student_name = intent.getStringExtra(StudentRegisterActivity.PREFERRED_NAME_KEY);
        ticket_status = intent.getStringExtra(STATUS_KEY);
        comment = intent.getStringExtra(COMMENT_KEY);




        //on clicking the Accept bottom, create
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ticket_status != "submitted") {
                    mBtnAccept.setError("Ticket Status Incorrect.");
                } else {

                    fStore = FirebaseFirestore.getInstance();
                    DocumentReference documentReference = fStore.collection(MEETING_TABLE_KEY).document();
                    meeting_id = documentReference.getId();

                    //Store fields of meeting to Firebase, under collection "meetings"
                    Map<String, Object> meetings = new HashMap<>();
                    meetings.put(STATUS_KEY, meetingUninitiated);
                    meetings.put(COMMENT_KEY, comment);
                    meetings.put(TIME_PERIOD_KEY, time);
                    meetings.put(STUDENT_ID_KEY, student_id);
                    meetings.put(TUTOR_ID_KEY, tutor_id);

                    documentReference.set(meetings)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Meeting Created.", Toast.LENGTH_SHORT).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("AcceptTicket", "Error creating meeting document", e);
                                }
                            });


                    /*create meeting object
                    meeting = new Meeting();
                    meeting.setComment(comment);
                    meeting.setMeeting_id(meeting_id);
                    meeting.setStatus(meetingUninitiated);
                    meeting.setStudent_id(student_id);
                    meeting.setTutor_id(tutor_id);*/


                    //update Ticket status
                    DocumentReference dRTicket = fStore.collection("student_ticket").document(ticket_id);
                    dRTicket.update(SubmitTicketActivity.STATUS_KEY, "finished").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("AcceptTicket", "Ticket Status successfully updated!");
                        }
                    });
                }
            }
        });



















        // Click backArrow to return to TutorPageActivity
        backArrow = findViewById(R.id.backarrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AcceptTicketActivity.this, TutorPageActivity.class);
                startActivity(intent);
            }
        });

    }
}