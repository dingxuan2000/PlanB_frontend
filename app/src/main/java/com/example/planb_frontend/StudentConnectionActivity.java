package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planb_backend.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StudentConnectionActivity extends AppCompatActivity {

    private ImageView mainB;
    private ImageView historyB;
    private ImageView profileB;
    private User studentUser;

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    private ListView UpcomingList;
    private ListView OngoingList;

    private ArrayList<String> upcoming_tutor_name= new ArrayList<String>();
    private ArrayList<String> upcoming_course_code= new ArrayList<String>();
    private ArrayList<String> upcoming_time= new ArrayList<String>();
    private ArrayList<String> upcoming_tutor_phone= new ArrayList<String>();
    private ArrayList<String> upcoming_meeting_id = new ArrayList<String>();

    private ArrayList<String> ongoing_tutor_name= new ArrayList<String>();
    private ArrayList<String> ongoing_course_code= new ArrayList<String>();
    private ArrayList<String> ongoing_time= new ArrayList<String>();
    private ArrayList<String> ongoing_tutor_phone= new ArrayList<String>();
   // private ArrayList<String> ongoing_meeting_id = new ArrayList<String>();
   // private ArrayList<String> ongoing_tutor_id = new ArrayList<String>();

    private String curr_tutor_id;
    private String student_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_student);

        Intent prevIntent = getIntent();
        studentUser = (User) prevIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);
        student_id = studentUser.getId();

        Query queryAll = fStore.collection(AcceptTicketActivity.MEETING_TABLE_KEY).whereEqualTo(AcceptTicketActivity.STUDENT_ID_KEY, student_id);
        Query queryUpcoming;
        Query queryOngoing;


        if(queryAll != null) {
            queryUpcoming = queryAll.whereEqualTo(AcceptTicketActivity.STATUS_KEY, AcceptTicketActivity.meetingUninitiated);
            queryOngoing = queryAll.whereEqualTo(AcceptTicketActivity.STATUS_KEY, AcceptTicketActivity.meetingOngoing);

            if (queryUpcoming != null) {
                StudentUpcomingCustomListView SUCListView = new StudentUpcomingCustomListView(this, upcoming_tutor_name, upcoming_course_code, upcoming_time, upcoming_tutor_phone, upcoming_meeting_id, studentUser);
                queryUpcoming.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                curr_tutor_id = doc.get("tutor_id").toString();
                                DocumentReference docref = fStore.collection("users").document(curr_tutor_id);
                                docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot docc = task.getResult();
                                            if (docc.exists()) {
                                                upcoming_meeting_id.add(doc.getId());
                                                upcoming_tutor_name.add(docc.getString(StudentRegisterActivity.PREFERRED_NAME_KEY));
                                                upcoming_tutor_phone.add(docc.getString(StudentRegisterActivity.PHONE_NUMBER_KEY));
                                                upcoming_course_code.add(doc.getString(AcceptTicketActivity.COURSE_KEY));
                                                upcoming_time.add(doc.getString(AcceptTicketActivity.TIME_PERIOD_KEY));
                                                UpcomingList.setAdapter(SUCListView);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

                //set the screen
                UpcomingList = (ListView) findViewById(R.id.student_upcoming_listview);
                UpcomingList.setAdapter(SUCListView);

            } else {
                Toast.makeText(getApplicationContext(), "No Upcoming Meeting", Toast.LENGTH_SHORT).show();
            }

            if (queryOngoing != null) {
                // To be Implemented
                //Use setText to set fields
                StudentOngoingCustomListView SOCustomListView = new StudentOngoingCustomListView(this, ongoing_tutor_name, ongoing_course_code, ongoing_time, ongoing_tutor_phone);
                queryOngoing.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                curr_tutor_id = doc.get("tutor_id").toString();
                                DocumentReference docref = fStore.collection("users").document(curr_tutor_id);
                                docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot docc = task.getResult();
                                            if (docc.exists()) {
                                               // ongoing_student_id.add(docc.getId());
                                               // ongoing_meeting_id.add(doc.getId());
                                                ongoing_tutor_name.add(docc.getString(StudentRegisterActivity.PREFERRED_NAME_KEY));
                                                ongoing_tutor_phone.add(docc.getString(StudentRegisterActivity.PHONE_NUMBER_KEY));
                                                ongoing_course_code.add(doc.getString(AcceptTicketActivity.COURSE_KEY));
                                                ongoing_time.add(doc.getString(AcceptTicketActivity.TIME_PERIOD_KEY));
                                                OngoingList.setAdapter(SOCustomListView);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

                OngoingList = (ListView) findViewById(R.id.student_ongoing_listview);
                OngoingList.setAdapter(SOCustomListView);
            } else {
                Toast.makeText(getApplicationContext(), "No Ongoing Meeting", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "No Available Meeting", Toast.LENGTH_SHORT).show();
        }









        mainB = findViewById(R.id.student_main_btn);
        mainB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentConnectionActivity.this, StudentPageActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,studentUser);
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.student_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentConnectionActivity.this, StudentHistoryActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,studentUser);
                startActivity(intent);
            }
        });

        profileB = findViewById(R.id.student_profile_btn);
        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentConnectionActivity.this, StudentProfileActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,studentUser);
                startActivity(intent);
            }
        });

    }
}
