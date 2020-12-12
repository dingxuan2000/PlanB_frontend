package com.example.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class StudentProfileActivity extends AppCompatActivity {
    private ImageView connectionB;
    private ImageView historyB;
    private ImageView mainB;
    private ImageView logout;
    private ImageView submit_ticket;
    private User user;
    private static final String TAG = "StudentProfile";
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    private TextView studentName;
    private TextView gradeStanding;
    private TextView major;
    private TextView phoneNumber;
    private TextView email;

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_student);

        Intent prevIntent = getIntent();
        user = (User) prevIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);


        fAuth = FirebaseAuth.getInstance();

        studentName = findViewById(R.id.student_name);
        studentName.setText(user.getPreferred_name());
        gradeStanding = findViewById(R.id.grade_standing);
        gradeStanding.setText(user.getClass_standing());
        major = findViewById(R.id.major);
        major.setText(user.getMajor());
        phoneNumber = findViewById(R.id.phone_number);
        phoneNumber.setText(user.getPhone_number());
        email = findViewById(R.id.email);
        email.setText(user.getEmail());

        connectionB = findViewById(R.id.student_connection_btn);
        connectionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentProfileActivity.this, StudentConnectionActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,user);
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.student_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentProfileActivity.this, StudentHistoryActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,user);
                startActivity(intent);
            }
        });

        mainB = findViewById(R.id.student_main_btn);
        mainB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentProfileActivity.this, StudentPageActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,user);
                startActivity(intent);
            }
        });

        //Logout verify box
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentProfileActivity.this);
                builder.setIcon(R.drawable.warning);
                builder.setTitle("Sign Out");
                builder.setMessage("Are you sure that you want to sign out?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        fAuth.signOut();
                        Intent intent = new Intent(StudentProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Successfully signed out", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Toast.makeText(StudentProfileActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        Intent preIntent = getIntent();
        //if the user already has a ticket in firebase, then stop jump to the SubmitTicket page.
        //1.check if the userId has already in student_collection
        user = (User)preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);
        String userId = user.getId();
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





