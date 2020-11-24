package com.example.planb_frontend;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.planb_backend.User;
import com.example.planb_frontend.StudentRegisterActivity;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class SubmitTicketActivity extends AppCompatActivity {

    public static final String COURSE_CODE_KEY = "course_code";
    public static final String STATUS_KEY = "status"; //不确定要不要, "submitted" or "finished"
    public static final String TUTOR_PREFERENCE_KEY = "tutor_preferrence"; //online or offline
    public static final String TIME_PREFERENCE_KEY = "time_preference"; // choose time(1hr, 2hr..)
    public static final String COMMENT_KEY = "comment";

    FirebaseFirestore fStore;

    //声明控件
    private EditText mEtCourseCode;
    private TextView mTvOnlineTutor;
    private TextView mTvOfflineTutor;

    private TextView mTvThirtenMin;
    private TextView mTvOneHr;
    private TextView mTvTwoHrs;

    private EditText mEtComment;
    private Button mBtnSubmit;

    private User newUser;

    public static final  String TAG="SubmitTicketActivity";

    String tutorPre = "";
    String timePre = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_ticket);

       //找到控件
        mEtCourseCode = findViewById(R.id.search);
        mTvOnlineTutor = findViewById(R.id.Online_Tutoring);
        mTvOfflineTutor = findViewById(R.id.Offline_Tutoring);
        mTvThirtenMin = findViewById(R.id.thirtyMinutes);
        mTvOneHr = findViewById(R.id.oneHour);
        mTvTwoHrs = findViewById(R.id.twoHours);
        mEtComment = findViewById(R.id.comment);
        mBtnSubmit = findViewById(R.id.submit);

        //get the tutor preference by using onClick
        mTvOnlineTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 tutorPre = mTvOnlineTutor.getText().toString();

            }
        });

        mTvOfflineTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutorPre = mTvOfflineTutor.getText().toString();

            }
        });

        mTvThirtenMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePre = mTvThirtenMin.getText().toString();

            }
        });

        mTvOneHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePre = mTvOneHr.getText().toString();

            }
        });


        //get the time preference by using onClick
        mTvTwoHrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 timePre = mTvTwoHrs.getText().toString();

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
                    String userId = intent.getStringExtra(StudentRegisterActivity.GET_USER_KEY);
                    //test: userId: null
                    System.out.println("userId:" + userId);
                    //error: java.lang.NullPointerException: Provided document path must not be null.
                    DocumentReference documentReference = fStore.collection(StudentRegisterActivity.USERS_TABLE_KEY).document(userId);
                    //in firebase, the collection name is student_ticket
                    Map<String, Object> student_ticket = new HashMap<>();
                    //save the current userId
                    student_ticket.put(StudentRegisterActivity.USER_ID_KEY,userId);
                    student_ticket.put(COURSE_CODE_KEY, courseCode);

                    student_ticket.put(TUTOR_PREFERENCE_KEY, tutorPre);
                    student_ticket.put(TIME_PREFERENCE_KEY, timePre);
                    student_ticket.put(COMMENT_KEY, comment);

                    //不确定是要用addOnSuccessListener还是addOnCompleteListener!
                    documentReference.set(student_ticket).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Submitted Ticket Successful", Toast.LENGTH_SHORT).show();
                            //needs to set status to be "submitted"
                            String status = "submitted";
                            student_ticket.put(STATUS_KEY, status);

//                            //after submit ticket sucessful, return to the student_mainPage
                            Intent intent = new Intent(getApplicationContext(), StudentPageActivity.class);
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





