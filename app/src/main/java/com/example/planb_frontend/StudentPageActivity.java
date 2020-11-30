package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planb_backend.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import static com.example.planb_backend.service.FCMService.FCM_TAG;

public class StudentPageActivity extends AppCompatActivity {

    private ImageView connectionB;
    private ImageView historyB;
    private ImageView profileB;
    private ImageView submit_ticketB;
    private SearchView searchView;

    private User user;

    //create custom adapter
    ListView tutor_rank;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    ArrayList<String> tutor_name = new ArrayList<String>();
    ArrayList<String> tutor_major = new ArrayList<String>();
    ArrayList<String> tutor_grade = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent preIntent = getIntent();
        //if the user already has a ticket in firebase, then stop jump to the SubmitTicket page.
        //1.check if the userId has already in student_collection
        user = (User)preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);
        String userId = user.getId();

        /**
         * Subscribe to current user's message channel
         * */
        FirebaseMessaging.getInstance().subscribeToTopic("topic-" + userId)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = task.isSuccessful() ? getString(R.string.subscription_success) : getString(R.string.subscription_failure);
                        Log.d(FCM_TAG, msg);
                    }
                });


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

        //search view
//        searchView = findViewById(R.id.search_bar);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
//            @Override
//            public boolean onQueryTextSubmit(String text){
//                StudentcustomListView.getFilter().filter(text);
//                tutor_rank.setAdapter(StudentcustomListView);
//                return true;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText){
//                StudentcustomListView.getFilter().filter(newText);
//                return false;
//            }
//        });


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
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,user);
                startActivity(intent);
            }
        });

        submit_ticketB = findViewById(R.id.student_submit_ticket);
        submit_ticketB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("userId:" + userId);
                DocumentReference documentReference = fStore.collection(SubmitTicketActivity.TICKET_TABLE_KEY).document(userId);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            //这个是可以的，但还要修改一下！！
                            if(doc.exists()){
                                Toast.makeText(getApplicationContext(), "Sorry, you can't submit ticket again!", Toast.LENGTH_SHORT).show();
                                Log.d("Document",doc.getString("user_id")+" "+doc.get("course_code").toString());
                                Intent intent = new Intent(StudentPageActivity.this, StudentPageActivity.class);
                                intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(StudentPageActivity.this, SubmitTicketActivity.class);
                                intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
                                startActivity(intent);
                            }

                        }
                    }
                });

//                    Intent intent = new Intent(StudentPageActivity.this, SubmitTicketActivity.class);
//                    intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
//                    startActivity(intent);

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
