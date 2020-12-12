package com.example.controller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.model.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import com.example.services.request.HttpRequestTask;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    public static final String COURSE_KEY = "course";
    public static final String MEETING_TABLE_KEY = "meetings";

    public static final  String TAG="AcceptTicketActivity";


    private FirebaseFirestore fStore;

    //Meeting Status
    public static final String meetingUninitiated = "Uninitiated";
    public static final String meetingOngoing = "Ongoing";

    private String meeting_id;
    private String student_id;
    private String tutor_id;
    private String student_name;
    private String time;   //meeting time period
    private String course;
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
        mEtCourseCode = (TextView) findViewById(R.id.search);
        mBtnAccept = (Button) findViewById(R.id.AcceptTicket);
        mEtComment = (TextView) findViewById(R.id.comment);
        mEtMeetingPreference = (TextView) findViewById(R.id.meeting_preference);
        mEtTimeSlot = (TextView) findViewById(R.id.Time_Slot);


        fStore = FirebaseFirestore.getInstance();



        //加Tutor和student手机号
        //get fields from TutorPageActivity with getExtra()
        Intent intent = getIntent();
        tutorUser = (User)intent.getSerializableExtra("tutorUser");
        tutor_id = tutorUser.getId();
        student_id = intent.getStringExtra(STUDENT_ID_KEY);
        time = intent.getStringExtra(TIME_PERIOD_KEY);
        comment = intent.getStringExtra(COMMENT_KEY);
        course = intent.getStringExtra(SubmitTicketActivity.COURSE_CODE_KEY);
        student_name = intent.getStringExtra(StudentRegisterActivity.PREFERRED_NAME_KEY);
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
                System.out.println("ticketId: "+ tutor_id);
                fStore.collection("meetings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            //count the total number of meetings
                            int sizeDoc = 0;
                            for(QueryDocumentSnapshot document: task.getResult()){
                                sizeDoc++;
                            }

                            int count = 0;
                            for(QueryDocumentSnapshot document: task.getResult()){
                                count++;
                                System.out.println("sizeDoc: "+ sizeDoc);
                                System.out.println("count: " + count);
                                System.out.println("document.get(tutor_id): " + document.get("tutor_id"));

                                Map<String, Object> map = document.getData();
                                //case 3
                                if(document.get("tutor_id") == null && map.size() != 0){
                                    System.out.println("the user doesn't accept a ticket2.0!");
                                    System.out.println("compare ids: " + document.get("tutor_id"));
                                    System.out.println(tutor_id);
                                    DocumentReference documentReference = fStore.collection(MEETING_TABLE_KEY).document();
                                    acceptTicket(documentReference);
                                    break;
                                }


                                //case2: if document's field is null, then skip this initialization empty meeting!
                                if(map.size() == 0){
                                    if(count == sizeDoc){
                                        System.out.println("the user doesn't have a ticket!");
                                        System.out.println("compare ids: " + document.get("tutor_id"));//null
                                        System.out.println(tutor_id);
                                        DocumentReference documentReference = fStore.collection(MEETING_TABLE_KEY).document();
                                        acceptTicket(documentReference);
                                        break;
                                    }
                                    //skip the null field
                                    Log.d(TAG, "Initial documnet is empty! Skip it");
                                    continue;
                                }



                                //case 3:
                                if(count == sizeDoc){
                                    System.out.println("we have reached the end!");
                                    System.out.println("Passed in: " + tutor_id);
                                    //then check the last document's tutor_id, if not equal, then allow the tutor to accept the ticket
                                    if(!(document.get("tutor_id").equals(tutor_id))){
                                        System.out.println("the user doesn't accept any ticket!");
                                        System.out.println("compare ids: " + document.get("tutor_id"));
                                        System.out.println(tutor_id);
                                        DocumentReference documentReference = fStore.collection(MEETING_TABLE_KEY).document();
                                        acceptTicket(documentReference);
                                        break;
                                    }else{
                                        System.out.println("the user has already accepted a ticket: " + document.get("tutor_id"));
                                        Toast.makeText(getApplicationContext(), "Sorry, you can't accept ticket again!", Toast.LENGTH_SHORT).show();
                                        break;

                                    }
                                }
                                Log.d(TAG,document.get("tutor_id") + "=>" + document.getData());
                                if(!(document.get("tutor_id").equals(tutor_id))){
                                    System.out.println(document.get("tutor_id"));
                                    System.out.println(tutor_id);
                                    continue;
                                }
                                if(document.get("tutor_id").equals(tutor_id)) {
                                    System.out.println("the user has already accepted a ticket: " + document.get("tutor_id"));
                                    Toast.makeText(getApplicationContext(), "Sorry, you can't accept ticket again!", Toast.LENGTH_SHORT).show();
                                    break;
                                }

                            }

                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
                //1.首先判断这个tutor user是否已经有meeting在meetings里了，
                //   如果没有的话，可以create a new meeting document;
                //   如果有的话，toast.maketext("sorry, you can't accept one more ticket!");




//                //delete Ticket from database
//                DocumentReference ticketRef = fStore.collection("student_ticket").document(ticket_id);
//                ticketRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("AcceptTicket", "Ticket is accepted and deleted! ");
//                    }
//                });

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

    //accept ticket and create meeting and delete ticket
    public void acceptTicket(DocumentReference documentReference){
        meeting_id = documentReference.getId();
        System.out.println("meetingId: "+ meeting_id);


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
                                Log.e("WTF", "SUCCESS ENTER");
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
                        intent.putExtra(StudentRegisterActivity.GET_USER_KEY, tutorUser);
                        startActivity(intent);
                    }
                });
        //                    .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d("AcceptTicket", "Error creating meeting document", e);
//                            }
//                        });

        //delete Ticket from database
//        DocumentReference ticketRef = fStore.collection("student_ticket").document(ticket_id);
//        ticketRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d("AcceptTicket", "Ticket is accepted and deleted! ");
//            }
//        });

    }

}