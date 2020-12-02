package com.example.planb_frontend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class TutorConnectionActivity extends AppCompatActivity {

    private ImageView mainB;
    private ImageView historyB;
    private ImageView profileB;
    private LinearLayout meet;
    private LinearLayout upcoming;

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    private ListView UpcomingList;


    private ArrayList<String> student_name= new ArrayList<String>();
    private ArrayList<String> course_code= new ArrayList<String>();
    private ArrayList<String> time= new ArrayList<String>();
    private ArrayList<String> student_phone= new ArrayList<String>();

    private String tutor_id;
    private String student_id;
    private boolean yesUpcoming = false;





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
                    TutorCCustomListView TCListView = new TutorCCustomListView(this, student_name, course_code, time, student_phone);
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
                                                    student_name.add(docc.getString(StudentRegisterActivity.PREFERRED_NAME_KEY));
                                                    student_phone.add(docc.getString(StudentRegisterActivity.PHONE_NUMBER_KEY));
                                                    course_code.add(doc.getString(AcceptTicketActivity.COURSE_KEY));
                                                    time.add(doc.getString(AcceptTicketActivity.TIME_PERIOD_KEY));
                                                    UpcomingList.setAdapter(TCListView);
                                                  // Log.d("Upcoming", docc.getString("preferred_name") + " " + doc.get("course").toString());
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                //set the screen
                UpcomingList = (ListView) findViewById(R.id.upcoming_listview);
                UpcomingList.setAdapter(TCListView);
            }
            else{
                Toast.makeText(getApplicationContext(), "No Upcoming Meeting", Toast.LENGTH_SHORT).show();
            }

            if(queryOngoing!=null){
                // To be Implemented
                //Use setText to set fields

            }
            else{
                Toast.makeText(getApplicationContext(), "No Ongoing Meeting", Toast.LENGTH_SHORT).show();
            }


        }
        else{
            Toast.makeText(getApplicationContext(), "No Available Meeting", Toast.LENGTH_SHORT).show();
        }








       /*
        //Query meeting collection for student name and phone number
        Query queryAll = fStore.collection(AcceptTicketActivity.MEETING_TABLE_KEY).whereEqualTo(AcceptTicketActivity.TUTOR_ID_KEY, tutor_id);
      //  Query queryUpComing = queryAll.whereEqualTo(AcceptTicketActivity.STATUS_KEY, AcceptTicketActivity.meetingUninitiated);

        if(queryAll != null) {
            TutorCCustomListView TCListView = new TutorCCustomListView(this, student_name, course_code, time, student_phone);
            noUpcoming = false;
            queryAll.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            //grab student_id from meeting collection with tutor_id
                            student_id = doc.get(AcceptTicketActivity.STUDENT_ID_KEY).toString();
                            DocumentReference docref = fStore.collection("users").document(student_id);
                            docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        //docc refers to student User, doc refers to the Meeting
                                        DocumentSnapshot docc = task.getResult();
                                        if (docc.exists()) {

                                            student_name.add(docc.getString(StudentRegisterActivity.PREFERRED_NAME_KEY));
                                            student_phone.add(docc.getString(StudentRegisterActivity.PHONE_NUMBER_KEY));
                                            course_code.add(doc.getString(AcceptTicketActivity.COURSE_KEY));
                                            time.add(doc.getString(AcceptTicketActivity.TIME_PERIOD_KEY));

                                            UpcomingList.setAdapter(TCListView);
                                            Log.d("Upcoming", docc.getString("preferred_name") + " " + doc.get("course_code").toString());
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });

            //set the screen
            super.onCreate(savedInstanceState);
            setContentView(R.layout.connection_tutor);

            UpcomingList = (ListView) findViewById(R.id.upcoming_listview);
            UpcomingList.setAdapter(TCListView);

        }
        else{
            Toast.makeText(getApplicationContext(), "No Upcoming Meeting", Toast.LENGTH_SHORT).show();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.connection_tutor);
        }*/








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



/*
        //Meet verify box
        meet = findViewById(R.id.meet);
        meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(TutorProfileActivity.this, LoginActivity.class);
//                startActivity(intent);
                AlertDialog.Builder builder = new AlertDialog.Builder(TutorConnectionActivity.this);
                builder.setIcon(R.drawable.warning);
                builder.setTitle("Verify");
                builder.setMessage("Are you sure that you finish the meeting?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Intent intent = new Intent(TutorConnectionActivity.this, StudentHistoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Toast.makeText(TutorConnectionActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });






        //Upcoming verify box
        upcoming = findViewById(R.id.upcoming);
        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(TutorProfileActivity.this, LoginActivity.class);
//                startActivity(intent);
                AlertDialog.Builder builder = new AlertDialog.Builder(TutorConnectionActivity.this);
                builder.setIcon(R.drawable.warning);
                builder.setTitle("Verify");
                builder.setMessage("Are you sure that you meet the student?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Intent intent = new Intent(TutorConnectionActivity.this, TutorConnectionActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Toast.makeText(TutorConnectionActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        */



    }
}


