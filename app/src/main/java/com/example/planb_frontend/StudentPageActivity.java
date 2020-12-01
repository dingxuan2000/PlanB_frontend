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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Map;

import static com.example.planb_backend.service.FCMService.FCM_TAG;

public class StudentPageActivity extends AppCompatActivity {

    private ImageView connectionB;
    private ImageView historyB;
    private ImageView profileB;
    private ImageView submit_ticketB;
    private SearchView searchView;

    private User user;
    public static final  String TAG="StudentPageActivity";

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
        System.out.println("once we entered homepage, the userId: "+ userId);

//        Log.d("DEBUG", userId);
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
        fStore.collection("users")
                .whereEqualTo("type", "tutor")
                .orderBy("rating", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        tutor_name.add(doc.getString("preferred_name"));
                        tutor_major.add(doc.getString("major"));
                        //tutor_grade.add(doc.get("course_code").toString());
                        tutor_grade.add(doc.getString("class_standing"));
                        tutor_rank.setAdapter(StudentcustomListView);
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
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,user);
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.student_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentPageActivity.this, StudentHistoryActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY,user);
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
                System.out.println("Once we loginned in again, userId:" + userId);
                fStore.collection("student_ticket").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            int sizeDoc = 0;
                            for(QueryDocumentSnapshot document: task.getResult()){
                                sizeDoc++;

                                //if document.getData()中的userId == login pass进来的userId,
                                //then 提示错误，不能跳转页面
                                //else: 可以跳转页面
//                                Log.d(TAG,document.get("user id") + "=>" + document.getData());
//                                if(document.get("user id").equals(userId)){
//                                    System.out.println("the user has already had a ticket: " + document.get("user id"));
//                                    Toast.makeText(getApplicationContext(), "Sorry, you can't submit ticket again!", Toast.LENGTH_SHORT).show();

//                                    Intent intent = new Intent(StudentPageActivity.this, StudentPageActivity.class);
//                                    intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
//                                    startActivity(intent);
//                                    StudentPageActivity.this.finish(); //can't finish!!
//
                                //不跳转页面，保留在本activity, 暂时不知道怎么实现？？
                                //}

                            }

                            int count = 0;
                            for(QueryDocumentSnapshot document: task.getResult()){
                                count++;
                                System.out.println("sizeDoc: "+ sizeDoc);
                                System.out.println("count: " + count);
                                System.out.println("document.get(user id): " + document.get("user id"));
                                //if document.get("user is") is null, then allows the user to submit ticket!
                                Map<String, Object> map = document.getData();
                                if(map.size() == 0){
                                    //skip the null field
                                    Log.d(TAG, "Initial documnet is empty! Skip it");
                                    continue;
                                }
                                if(document.get("user id") == null){
                                    System.out.println("the user doesn't have a ticket2.0!");
                                    System.out.println("compare ids: " + document.get("user id"));
                                    System.out.println(userId);
                                    Intent intent = new Intent(getApplicationContext(), SubmitTicketActivity.class);
                                    intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
                                    startActivity(intent);
                                    break;
                                }
                                if(count == sizeDoc){
                                    System.out.println("we have reached the end!");
                                    System.out.println("Passed in: " + userId);
                                    //then check the last document's user id
                                    if(!(document.get("user id").equals(userId))){
                                        System.out.println("the user doesn't have a ticket!");
                                        System.out.println("compare ids: " + document.get("user id"));
                                        System.out.println(userId);
                                        Intent intent = new Intent(getApplicationContext(), SubmitTicketActivity.class);
                                        intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
                                        startActivity(intent);
                                        break;
                                    }else{
                                        System.out.println("the user has already had a ticket: " + document.get("user id"));
                                        Toast.makeText(getApplicationContext(), "Sorry, you can't submit ticket again!", Toast.LENGTH_SHORT).show();
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
                                    System.out.println("the user has already had a ticket: " + document.get("user id"));
                                    Toast.makeText(getApplicationContext(), "Sorry, you can't submit ticket again!", Toast.LENGTH_SHORT).show();
                                    break;
                                }


                            }
                            //test
//                            System.out.println("the user doesn't have a ticket: " + userId);
//                            Intent intent = new Intent(StudentPageActivity.this, SubmitTicketActivity.class);
//                            intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
//                            startActivity(intent);
                        }else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

//                String ticketId = preIntent.getStringExtra(SubmitTicketActivity.GET_TICKET_KEY);
//                System.out.println("when we come back to homepage, ticketId: " + ticketId);
//                DocumentReference documentReference = fStore.collection(SubmitTicketActivity.TICKET_TABLE_KEY).document(ticketId);



//                fStore.collection("student_ticket").whereNotEqualTo("user id", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for (QueryDocumentSnapshot doc : task.getResult()) {
//                                if (doc.exists()) {
//                                    Log.d("Document", doc.getString("user id") + " " + doc.get("course_code").toString());
////                                  Log.e("tag", task.getException().toString());
//                                    Intent intent = new Intent(StudentPageActivity.this, SubmitTicketActivity.class);
//                                    intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
//                                    startActivity(intent);
//                                }
//                            }
//                        }
//                    }
//                });
//
//                //我需要从firebase中的每个ticket中查找是否有这个userId
//                fStore.collection("student_ticket").whereEqualTo("user id", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()) {
//                            for (QueryDocumentSnapshot doc : task.getResult()) {
//                                //这个是可以的，但还要修改一下！！
//
//                                if (doc.exists()) {
//                                    Log.d("Document", doc.getString("user id") + " " + doc.get("course_code").toString());
////                                    Log.e("tag", task.getException().toString());
//                                    Toast.makeText(getApplicationContext(), "Sorry, you can't submit ticket again!", Toast.LENGTH_SHORT).show();
//
//                                    Intent intent = new Intent(StudentPageActivity.this, StudentPageActivity.class);
////                                intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
//                                    startActivity(intent);
//                                    StudentPageActivity.this.finish();
//                                }
//
//                            }
//                        }
//                    }
//                });


//
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
