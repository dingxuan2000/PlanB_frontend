package com.example.planb_frontend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.planb_backend.User;
//import com.example.planb_frontend.StudentRegisterActivity;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class SubmitTicketActivity extends AppCompatActivity {

    public static final String TICKET_TABLE_KEY = "student_ticket";
    public static final String COURSE_CODE_KEY = "course_code";
    public static final String STATUS_KEY = "status"; //不确定要不要, "submitted" or "finished"
    public static final String TUTOR_PREFERENCE_KEY = "tutor_preference"; //online or offline
    public static final String TIME_PREFERENCE_KEY = "time_preference"; // choose time(1hr, 2hr..)
    public static final String COMMENT_KEY = "comment";
//    public static final String GET_TICKET_KEY = "get_ticket";


    FirebaseFirestore fStore;

    //声明控件
    private ImageView mImArrow;
    private EditText mEtCourseCode;
    private TextView mTvOnlineTutor;
    private TextView mTvOfflineTutor;

    private TextView mTvThirtenMin;
    private TextView mTvOneHr;
    private TextView mTvTwoHrs;

    private Boolean play = false;

    private EditText mEtComment;
    private Button mBtnSubmit;

    private User user;

    public static final  String TAG="SubmitTicketActivity";

    String tutorPre = "";
    String timePre = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_ticket);

        //找到控件
        mImArrow = findViewById(R.id.backarrow);
        mEtCourseCode = findViewById(R.id.search_course);
        mTvOnlineTutor = findViewById(R.id.Online_Tutoring);
        mTvOfflineTutor = findViewById(R.id.Offline_Tutoring);
        mTvThirtenMin = findViewById(R.id.thirtyMinutes);
        mTvOneHr = findViewById(R.id.oneHour);
        mTvTwoHrs = findViewById(R.id.twoHours);
        mEtComment = findViewById(R.id.comment);
        mBtnSubmit = findViewById(R.id.submit);



        mImArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preIntent = getIntent();
//                            //after submit ticket sucessful, return to the student_mainPage
                Intent intent = new Intent(getApplicationContext(), StudentPageActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
                startActivity(intent);
            }
        });

        //get the tutor preference by using onClick
        mTvOnlineTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initially, set the button to unclicked condition
                if(play){
                    mTvOnlineTutor.setBackgroundResource(R.drawable.shape_green);
                    mTvOfflineTutor.setBackgroundResource(R.drawable.shape_grey);
                    tutorPre = mTvOnlineTutor.getText().toString();
                }
                else {
                    mTvOnlineTutor.setBackgroundResource(R.drawable.shape_grey);
                }
                play = !play;

            }
        });

        mTvOfflineTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(play){
                    mTvOfflineTutor.setBackgroundResource(R.drawable.shape_green);
                    mTvOnlineTutor.setBackgroundResource(R.drawable.shape_grey);
                    tutorPre = mTvOfflineTutor.getText().toString();
                }
                else {
                    mTvOfflineTutor.setBackgroundResource(R.drawable.shape_grey);
                }
                play = !play;

            }
        });

        mTvThirtenMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(play){
                    mTvThirtenMin.setBackgroundResource(R.drawable.shape_green);
                    mTvOneHr.setBackgroundResource(R.drawable.shape_grey);
                    mTvTwoHrs.setBackgroundResource(R.drawable.shape_grey);

                    timePre = mTvThirtenMin.getText().toString();
                }
                else {
                    mTvThirtenMin.setBackgroundResource(R.drawable.shape_grey);
                }
                play = !play;


            }
        });

        mTvOneHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(play){
                    mTvOneHr.setBackgroundResource(R.drawable.shape_green);
                    mTvThirtenMin.setBackgroundResource(R.drawable.shape_grey);
                    mTvTwoHrs.setBackgroundResource(R.drawable.shape_grey);

                    timePre = mTvOneHr.getText().toString();
                }
                else {
                    mTvOneHr.setBackgroundResource(R.drawable.shape_grey);
                }
                play = !play;


            }
        });


        //get the time preference by using onClick
        mTvTwoHrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(play){
                    mTvTwoHrs.setBackgroundResource(R.drawable.shape_green);
                    mTvThirtenMin.setBackgroundResource(R.drawable.shape_grey);
                    mTvOneHr.setBackgroundResource(R.drawable.shape_grey);

                    timePre = mTvTwoHrs.getText().toString();
                }
                else {
                    mTvTwoHrs.setBackgroundResource(R.drawable.shape_grey);
                }
                play = !play;
            }
        });


        //Declare an instance of FirebaseFirestore, initialize cloud firestore
        fStore = FirebaseFirestore.getInstance();

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseCode = mEtCourseCode.getText().toString();
                //set status to be finished later. Once submit ticket sucessfully, set status to be
                //"submitted"

                String comment = mEtComment.getText().toString();

                if(TextUtils.isEmpty(courseCode)){
                    mEtCourseCode.setError("Please enter course code");
                }else if(TextUtils.isEmpty(tutorPre)){
                    mTvOnlineTutor.setError("Please choose your preferred tutor way");
                }else if(TextUtils.isEmpty(timePre)){
                    mTvThirtenMin.setError("Please choose your preferred time");
                }else if(TextUtils.isEmpty(comment)){
                    mEtComment.setError("Please enter the description of your questions");
                }else{
                    //TODO update to firebase
                    //get user id from the previous intent
                    Intent intent = getIntent();
                    System.out.println("intent:" + intent);
                    user = (User)intent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);

                    //if user is null, means that the user wants to submit instant ticket again. This is not allowed!!
//                    if (user == null){
//                        Toast.makeText(getApplicationContext(), "Sorry, you can't submit ticket again!", Toast.LENGTH_SHORT).show();
//                        Intent intent2 = new Intent(getApplicationContext(), StudentPageActivity.class);
//                        startActivity(intent2);
//                    }

                    //test: userId: null
                    String userId = user.getId();
                    System.out.println("userId:" + userId);
                    //error: java.lang.NullPointerException: Provided document path must not be null.
                    String ticketId = fStore.collection(TICKET_TABLE_KEY).document().getId();
                    System.out.println("ticketId: " + ticketId);
                    System.out.println("after document, ticketId: " + ticketId);
                    //in firebase, the collection name is student_ticket
                    Map<String, Object> student_ticket = new HashMap<>();
                    //save the current userId
                    student_ticket.put(StudentRegisterActivity.USER_ID_KEY,userId);
                    student_ticket.put(COURSE_CODE_KEY, courseCode);
                    student_ticket.put(TUTOR_PREFERENCE_KEY, tutorPre);
                    student_ticket.put(TIME_PREFERENCE_KEY, timePre);
                    student_ticket.put(COMMENT_KEY, comment);
                    String status = "null";
                    student_ticket.put(STATUS_KEY, status);

                    fStore.collection(TICKET_TABLE_KEY).document(ticketId).set(student_ticket).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Submitted Ticket Successful", Toast.LENGTH_SHORT).show();
                            //needs to set status to be "submitted"
                            student_ticket.put(STATUS_KEY, "submitted");
                            fStore.collection(TICKET_TABLE_KEY).document(ticketId).update(student_ticket).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"Updated the ticket");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "onFailure: ", e);
                                }
                            });
                            Intent preIntent = getIntent();
//                            //after submit ticket sucessful, return to the student_mainPage
                            Intent intent = new Intent(getApplicationContext(), StudentPageActivity.class);
                            intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
                            System.out.println("check in the addOnsuccess, ticketId: " + ticketId);
//                            intent.putExtra(GET_TICKET_KEY, ticketId);

                            startActivity(intent);
                        }

                    });


//                            .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            //display error message
//                            Log.d(TAG, "onFailure: Failed submitted ticket!");
//
//                        }
//                    });

                }

            }
        });


    }
}





