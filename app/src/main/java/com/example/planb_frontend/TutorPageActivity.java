package com.example.planb_frontend;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planb_backend.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TutorPageActivity extends AppCompatActivity {
    private ImageView connectionB;
    private ImageView historyB;
    private ImageView profileB;
    ListView listView;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    ListView lst;
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> time = new ArrayList<String>();
    ArrayList<String> major = new ArrayList<String>();
    ArrayList<String> course = new ArrayList<String>();

    //added list to hold ticket comments
    ArrayList<String> comment = new ArrayList<String>();
    ArrayList<String> student_id = new ArrayList<String>();
    ArrayList<String> ticket_id = new ArrayList<String>();
    //ArrayList<String> ticket_status = new ArrayList<String>();
    ArrayList<String> tutor_preference = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        User passUser = (User) intent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);
        Toast.makeText(getApplicationContext(), passUser.getId(), Toast.LENGTH_SHORT).show();

        CustomListView customListView=new CustomListView(this, names, time, major, course);
        fStore.collection("student_ticket").whereEqualTo("status", "submitted").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        DocumentReference docref = fStore.collection("users").document(doc.getId());
                        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot docc = task.getResult();
                                    if (docc.exists()){

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
                                        lst.setAdapter(customListView);
                                        Log.d("Document",docc.getString("preferred_name")+" "+doc.get("course_code").toString());
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tutor);



        lst=(ListView)findViewById(R.id.listview);
        lst.setAdapter(customListView);


        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("TutorRegisterActivity", "onItemClick, student name is: " + names.get(position));
                //Toast.makeText(TutorPageActivity.this, "you click on " + names.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(TutorPageActivity.this, AcceptTicketActivity.class);

                intent.putExtra("tutorUser", passUser);
                intent.putExtra(AcceptTicketActivity.STUDENT_ID_KEY, names.get(position));
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
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.tutor_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorPageActivity.this, TutorHistoryActivity.class);
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
    }
}