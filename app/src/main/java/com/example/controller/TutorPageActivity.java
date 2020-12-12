package com.example.controller;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TutorPageActivity extends AppCompatActivity implements TextWatcher {
    private ImageView connectionB;
    private ImageView historyB;
    private ImageView profileB;
    private Switch switchB;
    private EditText search;

    public static final  String TAG="TutorPageActivity";
    public static final String TUTOR_STATUS_KEY = "status";

    Boolean offcampus;

    ListView listView;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    ListView lst;
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> time = new ArrayList<String>();
    ArrayList<String> major = new ArrayList<String>();
    ArrayList<String> course = new ArrayList<String>();
    String tiName,tiMajor,tiCourse,tiTime;
    ArrayList<ticketInfo> tickets = new ArrayList<>();
    TutorCustomListView customListView;

    //added list to hold ticket comments
    ArrayList<String> comment = new ArrayList<String>();
    ArrayList<String> student_id = new ArrayList<String>();
    ArrayList<String> ticket_id = new ArrayList<String>();
    //ArrayList<String> ticket_status = new ArrayList<String>();
    ArrayList<String> tutor_preference = new ArrayList<String>();
    ArrayList<String> precourse = new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tutor);

        offcampus = true;
        Intent intent = getIntent();
        User passUser = (User) intent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);

        search = (EditText) findViewById(R.id.tutor_search_bar);
        lst=(ListView)findViewById(R.id.listview);
        search.addTextChangedListener(this);
        customListView=new TutorCustomListView(this, tickets);

        DocumentReference docreff = FirebaseFirestore.getInstance().collection("preferred_courses").document(passUser.getId());
        docreff.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> tasks) {
                if (tasks.isSuccessful()) {
                    DocumentSnapshot doccc = tasks.getResult();
                    if (doccc.exists()) {
                        precourse.add(doccc.getString("course_1"));
                        precourse.add(doccc.getString("course_2"));
                        precourse.add(doccc.getString("course_3"));
                        precourse.add(doccc.getString("course_4"));
                        precourse.add(doccc.getString("course_5"));
                        if(doccc.getString("status") == null){
                            offcampus = true;
                        }
                        else if (doccc.getString("status").equals("onCampus")){
                            offcampus = false;
                        }
                        switchB.setChecked(!offcampus);

                        fStore.collection("student_ticket").whereEqualTo("status", "submitted").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        DocumentReference docref = FirebaseFirestore.getInstance().collection("users").document(doc.get("user id").toString());
                                        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot docc = task.getResult();
                                                    if (docc.exists() && precourse.contains(doc.get("course_code").toString()) && offcampus == false) {

                                                        //newly added: comment
                                                        comment.add(doc.getString(SubmitTicketActivity.COMMENT_KEY));
                                                        student_id.add(docc.getString(StudentRegisterActivity.USER_ID_KEY));
                                                        ticket_id.add(doc.getId());
                                                        //ticket_status.add(doc.getString(SubmitTicketActivity.STATUS_KEY));
                                                        tutor_preference.add(doc.getString(SubmitTicketActivity.TUTOR_PREFERENCE_KEY));

                                                        names.add(docc.getString("preferred_name"));
                                                        major.add(docc.getString("major"));
                                                        time.add(doc.get("time_preference").toString());
                                                        course.add(doc.get("course_code").toString());

                                                        tiName = docc.getString("preferred_name");
                                                        tiMajor = docc.getString("major");
                                                        tiTime = doc.get("time_preference").toString();
                                                        tiCourse = doc.get("course_code").toString();
                                                        ticketInfo tI = new ticketInfo(tiName,tiMajor,tiCourse,tiTime);
                                                        tickets.add(tI);
                                                        lst.setAdapter(customListView);
                                                        Log.d("Document", docc.getString("preferred_name") + " " + doc.get("course_code").toString());
                                                    }else if (docc.exists() && precourse.contains(doc.get("course_code").toString()) && doc.get("tutor_preference").equals("Online Tutoring") && offcampus == true) {

                                                        //newly added: comment
                                                        comment.add(doc.getString(SubmitTicketActivity.COMMENT_KEY));
                                                        student_id.add(docc.getString(StudentRegisterActivity.USER_ID_KEY));
                                                        ticket_id.add(doc.getId());
                                                        //ticket_status.add(doc.getString(SubmitTicketActivity.STATUS_KEY));
                                                        tutor_preference.add(doc.getString(SubmitTicketActivity.TUTOR_PREFERENCE_KEY));

                                                        names.add(docc.getString("preferred_name"));
                                                        major.add(docc.getString("major"));
                                                        time.add(doc.get("time_preference").toString());
                                                        course.add(doc.get("course_code").toString());

                                                        tiName = docc.getString("preferred_name");
                                                        tiMajor = docc.getString("major");
                                                        tiTime = doc.get("time_preference").toString();
                                                        tiCourse = doc.get("course_code").toString();
                                                        ticketInfo tI = new ticketInfo(tiName,tiMajor,tiCourse,tiTime);
                                                        tickets.add(tI);
                                                        lst.setAdapter(customListView);
                                                        Log.d("Document", docc.getString("preferred_name") + " " + doc.get("course_code").toString());
                                                    }
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "onFailure: ", e);
                                            }
                                        });
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: ", e);
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        });







        lst.setAdapter(customListView);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("TutorRegisterActivity", "onItemClick, student name is: " + names.get(position));
                //Toast.makeText(TutorPageActivity.this, "you click on " + names.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(TutorPageActivity.this, AcceptTicketActivity.class);

                intent.putExtra("tutorUser", passUser);
                intent.putExtra(AcceptTicketActivity.STUDENT_ID_KEY, student_id.get(position));
                intent.putExtra(AcceptTicketActivity.COMMENT_KEY, comment.get(position));
                intent.putExtra(AcceptTicketActivity.TIME_PERIOD_KEY, time.get(position));
                intent.putExtra(SubmitTicketActivity.COURSE_CODE_KEY, course.get(position));
                intent.putExtra(StudentRegisterActivity.PREFERRED_NAME_KEY, names.get(position));
                //intent.putExtra("ticket_status", ticket_status.get(position));
                intent.putExtra("ticket_id", ticket_id.get(position));
                intent.putExtra(SubmitTicketActivity.TUTOR_PREFERENCE_KEY, tutor_preference.get(position));

                startActivity(intent);
            }
        });


        connectionB = findViewById(R.id.tutor_connection_btn);
        connectionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorPageActivity.this, TutorConnectionActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,passUser);
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.tutor_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorPageActivity.this, TutorHistoryActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,passUser);
                startActivity(intent);
            }
        });

        profileB = findViewById(R.id.tutor_profile_btn);
        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorPageActivity.this, TutorProfileActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,passUser);
                startActivity(intent);
            }
        });

        switchB = findViewById(R.id.switch1);
        //store the tutor's status in preferred_courses collection
        switchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check current state of a sWITCH (if switch checked, returns true)
                //default: false (offCampus)

                String tutorId = passUser.getId();
                System.out.println(tutorId);
                Map<String, Object> preferred_courses = new HashMap<>();
                preferred_courses.put(TUTOR_STATUS_KEY, "null");
                //Find the document through tutorId (for now).
                DocumentReference docRef = fStore.collection("preferred_courses").document(tutorId);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                ////store status: "offCampus"
                                if (offcampus == false) {
                                    preferred_courses.put(TUTOR_STATUS_KEY, "offCampus");
                                    //update the document
                                    fStore.collection("preferred_courses").document(tutorId).update(preferred_courses).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG,"Updated the ticket");
                                            Intent intent = new Intent(TutorPageActivity.this, TutorPageActivity.class);
                                            intent.putExtra(StudentRegisterActivity.GET_USER_KEY,passUser);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: ", e);
                                        }
                                    });
                                }else {
                                    preferred_courses.put(TUTOR_STATUS_KEY, "onCampus");
                                    //update the document
                                    fStore.collection("preferred_courses").document(tutorId).update(preferred_courses).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG,"Updated the ticket");
                                            Intent intent = new Intent(TutorPageActivity.this, TutorPageActivity.class);
                                            intent.putExtra(StudentRegisterActivity.GET_USER_KEY,passUser);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: ", e);
                                        }
                                    });
                                }
                            }else {
                                Log.d(TAG, "No such document");
                            }
                        }else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });




            }
        });
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.customListView.getFilter().filter(s);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}