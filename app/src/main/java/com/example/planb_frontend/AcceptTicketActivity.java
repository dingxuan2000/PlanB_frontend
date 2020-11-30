package com.example.planb_frontend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planb_backend.User;
import com.example.planb_backend.task.HttpRequestTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import android.content.Intent;
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

    private String meeting_id;
    private String student_id;
    private String tutor_id;
    private String student_name;
   // private String major;
    private String time;   //meeting time slot
    private String course;
   // private String ticket_status;
    private String ticket_id;
    private String comment;
    private String meeting_preference;    // online or offline meeting
    private User tutorUser;



    //控件
    private ImageView backArrow;
    private Button mBtnAccept;
    private TextView mEtCourseCode;
    //student name
    private TextView mEtIllustrate;
    private TextView mEtComment;
    private TextView mEtTimeSlot;
    private TextView mEtMeetingPreference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_ticket);


        //initialize components
        mEtIllustrate =  (TextView) findViewById(R.id.illustrate);
        mEtCourseCode = (EditText) findViewById(R.id.search);
        mBtnAccept = (Button) findViewById(R.id.AcceptTicket);
        mEtComment = (TextView) findViewById(R.id.comment);
        mEtMeetingPreference = (TextView) findViewById(R.id.meeting_preference);
        mEtTimeSlot = (TextView) findViewById(R.id.Time_Slot);


        fStore = FirebaseFirestore.getInstance();


        //get fields from TutorPageActivity with getExtra()
        Intent intent = getIntent();
        tutorUser = (User)intent.getSerializableExtra("tutorUser");
        tutor_id = tutorUser.getId();
        student_id = intent.getStringExtra(STUDENT_ID_KEY);
        time = intent.getStringExtra(TIME_PERIOD_KEY);
        comment = intent.getStringExtra(COMMENT_KEY);
        course = intent.getStringExtra(SubmitTicketActivity.COURSE_CODE_KEY);
        student_name = intent.getStringExtra(StudentRegisterActivity.PREFERRED_NAME_KEY);
        //ticket_status = intent.getStringExtra("ticket_status");
        ticket_id = intent.getStringExtra("ticket_id");
        meeting_preference = intent.getStringExtra(SubmitTicketActivity.TUTOR_PREFERENCE_KEY);


        //Fill components with new Info
        mEtIllustrate.setText(student_name);
        mEtCourseCode.setText(course);
        mEtMeetingPreference.setText(meeting_preference);
        mEtComment.setText(comment);
        mEtTimeSlot.setText(time);

        //on clicking the Accept bottom, create meeting
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    meetings.put(COURSE_KEY, course);

                    DocumentReference dRTicket = fStore.collection("student_ticket").document(ticket_id);


                    documentReference.set(meetings)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Meeting Created.", Toast.LENGTH_SHORT).show();
                            /**
                             * If successfully created meeting, send notification and delete ticket
                             * */
                            dRTicket.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot dr) {
                                    if (dr.exists()) {
                                        new HttpRequestTask().execute(dr.getString(StudentRegisterActivity.USER_ID_KEY), tutor_id, tutorUser.getPreferred_name());
                                    } else {
                                        Toast.makeText(getApplicationContext(), "error occurred", Toast.LENGTH_LONG).show();
                                    }

                                    dRTicket.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("AcceptTicket", "Ticket is accepted and deleted! ");
                                        }
                                    });
                                }
                            });

                            Intent intent = new Intent(AcceptTicketActivity.this, TutorConnectionActivity.class);
                            startActivity(intent);
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("AcceptTicket", "Error creating meeting document", e);
                                }
                            });
                }
            //}
        });




        // Click backArrow to return to TutorPageActivity
        backArrow = findViewById(R.id.backarrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AcceptTicketActivity.this, TutorPageActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY, tutorUser);
                startActivity(intent);
            }
        });

    }
}