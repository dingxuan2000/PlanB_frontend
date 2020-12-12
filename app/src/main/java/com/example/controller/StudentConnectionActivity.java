package com.example.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class StudentConnectionActivity extends AppCompatActivity {

    private ImageView mainB;
    private ImageView historyB;
    private ImageView profileB;
    private ImageView submit_ticket;
    private User studentUser;

    private User user;
    private static final String TAG="StudentConnection";

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
        if (studentUser != null) {
            student_id = studentUser.getId();
        }
        else {
            student_id = FirebaseAuth.getInstance().getUid();
        }


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


        Intent preIntent = getIntent();
        //if the user already has a ticket in firebase, then stop jump to the SubmitTicket page.
        //1.check if the userId has already in student_collection
//        user = (User)preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);
        String userId = student_id;
        submit_ticket = findViewById(R.id.submit_ticket);
        Boolean[] flag = {false};//initialize a boolean flag, if the user can submit ticket, then reset the flag to be true!
        submit_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lastIntent = getIntent();
                user = (User)lastIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);
                System.out.println("Once we loginned in again, userId:" + userId);

                fStore.collection("student_ticket").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            int sizeDoc = 0;
                            for(QueryDocumentSnapshot document: task.getResult()){
                                sizeDoc++;
                            }

                            int count = 0;
                            for(QueryDocumentSnapshot document: task.getResult()){
                                count++;
                                System.out.println("sizeDoc: "+ sizeDoc);
                                System.out.println("count: " + count);
                                System.out.println("document.get(user id): " + document.get("user id"));
                                //if document.get("user is") is null, then allows the user to submit ticket!

                                /** case 1: we only have one empty field in the collection,
                                 so the user id we get is null and it's an empty field, then reset the flag to be true
                                 means may be it can submit ticket. But we also need to check the meeting collection!
                                 **/
                                // count=1, so we only have the initial empty meeting!
                                Map<String, Object> map = document.getData();
                                if(document.get("user id") == null && map.size() != 0){
                                    System.out.println("the user doesn't have a ticket2.0!");
                                    flag[0] = true;
                                    System.out.println("case1, only have one empty field: " + flag[0]);
                                    break;
                                }
                                //case2: if the empty document is at the last one, then we need to allow the user
                                // to submit ticket!
                                if(map.size() == 0){
                                    if(count == sizeDoc){
                                        flag[0] = true;
                                        System.out.println("case2, empty field is the last one: " + flag[0]);
                                        break;
                                    }
                                    //skip the null field
                                    Log.d(TAG, "Initial documnet is empty! Skip it");
                                    continue;
                                }

                                if(count == sizeDoc){
                                    System.out.println("we have reached the end!");
                                    //then check the last document's user id
                                    if(!(document.get("user id").equals(userId))){
                                        flag[0] = true;
                                        System.out.println("case3, reached to the last one, still not find this userId: " + flag[0]);
                                        break;
                                    }else{
                                        flag[0] = false;
                                        break;

                                    }
                                }
                                Log.d(TAG,document.get("user id") + "=>" + document.getData());
                                if(!(document.get("user id").equals(userId))){
                                    System.out.println(document.get("user id"));
                                    System.out.println(userId);
                                    continue;
                                }
                                if(document.get("user id").equals(userId)) {
                                    flag[0] = false;
                                    break;
                                }


                            }
                            //then check the meeting collections!!
                            System.out.println("after traversing the tickets collection, the current flag is: "+flag[0]);
                            //test meeting collection!
                            fStore.collection("meetings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        int sizeDoc = 0;
                                        //first, get the size of meetings collection
                                        for(QueryDocumentSnapshot document: task.getResult()){
                                            sizeDoc++;
                                        }

                                        int count = 0;
                                        //traverse the meeting collection
                                        for(QueryDocumentSnapshot document: task.getResult()){
                                            count++;
                                            System.out.println("sizeDoc: "+ sizeDoc);
                                            System.out.println("count: " + count);
                                            System.out.println("document.get(student_id): " + document.get("student_id"));
                                            //if document.get("user is") is null, then allows the user to submit ticket!

                                            /** case 1: we only have one empty field in the collection,
                                             so the student_id we get is null and it's an empty field, then reset the flag to be true
                                             means may be it can submit ticket. But we also need to check the meeting collection!
                                             **/
                                            Map<String, Object> map = document.getData();
                                            if(document.get("student_id") == null && map.size() != 0){
                                                System.out.println("the user doesn't have a meeting2.0!");
                                                System.out.println("compare ids: " + document.get("student_id"));
                                                System.out.println(userId);
                                                //if previous flag is true, then also set flag to be true; else, it should still be false!
                                                if(flag[0] == true){
                                                    flag[0] = true;
                                                }else {
                                                    flag[0] = false;
                                                }
                                                System.out.println("meeting case1, only have one empty field: " + flag[0]);
                                                break;
                                            }
                                            //case2: if the empty document is at the last one, then we need to allow the user
                                            // to submit ticket!
                                            if(map.size() == 0){
                                                if(count == sizeDoc){
                                                    System.out.println("the user doesn't have a meeting!");
                                                    System.out.println("compare ids: " + document.get("student_id"));//null
                                                    System.out.println(userId);
                                                    //if previous flag is true, then also set flag to be true; else, it should still be false!
                                                    if(flag[0] == true){
                                                        flag[0] = true;
                                                    }else {
                                                        flag[0] = false;
                                                    }
                                                    System.out.println("meeting case2, empty field is the last one: " + flag[0]);
                                                    break;
                                                }
                                                //skip the null field
                                                Log.d(TAG, "Initial documnet is empty! Skip it");
                                                continue;
                                            }

                                            //case3: when reach to the last document!
                                            if(count == sizeDoc){
                                                System.out.println("we have reached the end!");
                                                System.out.println("Passed in: " + userId);
                                                //then check the last document's user id
                                                if(!(document.get("student_id").equals(userId))){
                                                    System.out.println("the user doesn't have a meeting!");
                                                    System.out.println("compare ids: " + document.get("student_id"));
                                                    System.out.println(userId);
                                                    //if previous flag is true, then also set flag to be true; else, it should still be false!
                                                    if(flag[0] == true){
                                                        flag[0] = true;
                                                    }else {
                                                        flag[0] = false;
                                                    }
                                                    break;
                                                }else{
                                                    flag[0] = false;
                                                    System.out.println("the user has already had a meeting: " + document.get("student_id"));
                                                    break;

                                                }
                                            }
                                            Log.d(TAG,document.get("student_id") + "=>" + document.getData());
                                            if(!(document.get("student_id").equals(userId))){
                                                System.out.println(document.get("student_id"));
                                                System.out.println(userId);
                                                continue;
                                            }
                                            if(document.get("student_id").equals(userId)) {
                                                flag[0] = false;
                                                break;
                                            }


                                        }
                                        //After traversing both two collections:
                                        System.out.println("after traversing both two collections, flag is: "+ flag[0]);
                                        if(flag[0] == true){
                                            Intent intent = new Intent(getApplicationContext(), SubmitTicketActivity.class);
                                            intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
                                            startActivity(intent);
                                        }else {
                                            Toast.makeText(getApplicationContext(), "Sorry, you can't submit ticket again!", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                        }else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

            }
        });

    }
}
