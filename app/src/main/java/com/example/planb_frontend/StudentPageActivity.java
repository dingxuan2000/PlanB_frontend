package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StudentPageActivity extends AppCompatActivity {

    private ImageView connectionB;
    private ImageView historyB;
    private ImageView profileB;
    private ImageView submit_ticketB;

    //create custom adapter
    ListView tutor_rank;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    ArrayList<String> tutor_name = new ArrayList<String>();
    ArrayList<String> tutor_major = new ArrayList<String>();
    ArrayList<String> tutor_grade = new ArrayList<String>();
//    String[] tutor_name = {"caiwei zhao","zanyuan yang","zihao chen"};
//    String[] tutor_major = {"cs","cs","cs"};
//    String[] tutor_grade = {"senior","senior","senior"};
//    Integer[] tutor_imgid = {R.drawable.tutor_img1,R.drawable.tutor_img2,R.drawable.tutor_img3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        studentCustomListView StudentcustomListView=new studentCustomListView(this, tutor_name, tutor_major, tutor_grade);
        fStore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                        tutor_name.add(docc.getString("preferred_name"));
                                        tutor_major.add(docc.getString("major"));
                                        //tutor_grade.add(doc.get("course_code").toString());
                                        tutor_grade.add(docc.getString("class_standing"));
                                        tutor_rank.setAdapter(StudentcustomListView);
                                        //Log.d("Document",docc.getString("preferred_name")+" "+doc.get("course_code").toString());
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });



        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_student);


        tutor_rank = findViewById(R.id.tutor_listview);
        tutor_rank.setAdapter(StudentcustomListView);

        connectionB = findViewById(R.id.student_connection_btn);
        connectionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentPageActivity.this, StudentConnectionActivity.class);
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.student_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentPageActivity.this, StudentHistoryActivity.class);
                startActivity(intent);
            }
        });

        profileB = findViewById(R.id.student_profile_btn);
        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentPageActivity.this, StudentProfileActivity.class);
                startActivity(intent);
            }
        });

        submit_ticketB = findViewById(R.id.student_submit_ticket);
        submit_ticketB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentPageActivity.this, StudentSubmitTicketActivity.class);
                startActivity(intent);
            }
        });

//        submit_ticketB = findViewById(R.id.student_submit_ticket);
//        submit_ticketB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent preIntent = getIntent();
////                //intent.getStringExtra(StudentRegisterActivity.GET_USER_KEY);
////                System.out.println("intent:" + intent); //intent:Intent { cmp=com.example.planb_frontend/.StudentPageActivity }
////                newUser = (User)intent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);
////                String userId = newUser.getId();
////                //test: userId还是: null？？？
////                System.out.println("userId:" + userId);
//
//                Intent intent = new Intent(getApplicationContext(), SubmitTicketActivity.class);
//                intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
//                startActivity(intent);
//            }
//        });



    }
}
