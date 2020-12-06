package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
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

public class StudentPageActivity extends AppCompatActivity implements TextWatcher {

    private ImageView connectionB;
    private ImageView historyB;
    private ImageView profileB;
    private ImageView submit_ticketB;

    //CustomListView Search
    private EditText search;
    ListView tutor_rank;
    private ArrayList<tutorInfo> tutorList= new ArrayList<>();
    private ArrayList<tutorCourse> tutorCourses = new ArrayList<>();
    String name,major,grade,course_1,course_2,course_3,course_4, tutorId,uid;
    private tutorCourse tC;
//    tutorInfo ti;
    StudentCustomListView myListview;

    private User user;
    public static final  String TAG="StudentPageActivity";

    //create custom adapter

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    ArrayList<String> tutor_name = new ArrayList<String>();
    ArrayList<String> tutor_major = new ArrayList<String>();
    ArrayList<String> tutor_grade = new ArrayList<String>();
    ArrayList<String> tutor_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_student);

        Intent preIntent = getIntent();
        //if the user already has a ticket in firebase, then stop jump to the SubmitTicket page.
        //1.check if the userId has already in student_collection
        user = (User)preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);
        String userId = user.getId();
        System.out.println("once we entered homepage, the userId: "+ userId);

        // Search Function
        search = (EditText) findViewById(R.id.student_search_bar);
        tutor_rank = (ListView) findViewById(R.id.tutor_listview);

        search.addTextChangedListener(this);

        //studentCustomListView StudentcustomListView=new studentCustomListView(this, tutor_name, tutor_major, tutor_grade);
        myListview = new StudentCustomListView(this, tutorList);

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


        fStore.collection(AddCourses.PREFERRED_COURSES_KEY).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot docCourse : task.getResult()){
                        uid = docCourse.getId();

                        course_1 = docCourse.getString("course_1");
                        course_2 = docCourse.getString("course_2");
                        course_3 = docCourse.getString("course_3");
                        course_4 = docCourse.getString("course_4");
                        //course_5 = docCourse.getString("course_5");
//                        Log.d("GETUID",uid);
//                        Log.d("GETCOURSE1",course_1);
//                        Log.d("GETCOURSE2",course_2);
//                        Log.d("GETCOURSE3",course_3);
//                        Log.d("GETCOURSE4",course_4);
                        tC = new tutorCourse(uid,course_1,course_2,course_3,course_4);
                        tutorCourses.add(tC);

                    }
                } else {
                    Log.e("tag", task.getException().toString());
                    Toast.makeText(getApplicationContext(), "Failing", Toast.LENGTH_SHORT).show();
                }
//                Log.d("DEBUG COURSE1","begin");
//                for(int i=0;i<tutorCourses.size();i++){
//                    Log.d("COURSES", tutorCourses.get(i).getUid()+tutorCourses.get(i).getCourse_1());
//                }
            }
         });

//        Log.d("DEBUG COURSE2","begin");
//        for(int i=0;i<tutorCourses.size();i++){
//            Log.d("COURSES", tutorCourses.get(i).getUid()+tutorCourses.get(i).getCourse_1());
//        }



        fStore.collection("users")
                .whereEqualTo("type", "tutor")
                .orderBy("rating", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
//                        tutor_name.add(doc.getString("preferred_name"));
//                        tutor_major.add(doc.getString("major"));
//                        //tutor_grade.add(doc.get("course_code").toString());
//                        tutor_grade.add(doc.getString("class_standing"));
//                        tutor_rank.setAdapter(StudentcustomListView);
                        name = doc.getString("preferred_name");
                        major = doc.getString("major");
                        grade = doc.getString("class_standing");
                        tutorId = doc.getString(StudentRegisterActivity.USER_ID_KEY);
                        course_1 = "";course_2 = "";course_3 = "";course_4 = "";

                        for(int i=0;i<tutorCourses.size();i++){
//                            System.out.println("EQUAL: "+tutorCourses.get(i).getUid() + tutorId);
                            if(tutorCourses.get(i).getUid().equals(tutorId)){
                                course_1 = tutorCourses.get(i).getCourse_1();
                                course_2 = tutorCourses.get(i).getCourse_2();
                                course_3 = tutorCourses.get(i).getCourse_3();
                                course_4 = tutorCourses.get(i).getCourse_4();
                            }
                        }

//                        course_1 = doc.getString("course_1");
//                        course_2 = doc.getString("course_2");
//                        course_3 = doc.getString("course_3");
//                        course_4 = doc.getString("course_4");
//                        course_5 = doc.getString("course_5");

                        tutorInfo ti = new tutorInfo(name, major,grade,course_1,course_2
                                ,course_3,course_4);
                        tutorList.add(ti);
                        tutor_rank.setAdapter(myListview);
                    }
                }
            }
        });

        tutor_rank = findViewById(R.id.tutor_listview);
        tutor_rank.setAdapter(myListview);


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
        Boolean[] flag = {false};//initialize a boolean flag, if the user can submit ticket, then reset the flag to be true!
        submit_ticketB.setOnClickListener(new View.OnClickListener() {
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
                                //这和我写的acceptTicketActivity的case2是一个意思!
                                Map<String, Object> map = document.getData();
                                if(document.get("user id") == null && map.size() != 0){
                                    System.out.println("the user doesn't have a ticket2.0!");
                                    flag[0] = true;
                                    System.out.println("case1, only have one empty field: " + flag[0]);
//                                    Intent intent = new Intent(getApplicationContext(), SubmitTicketActivity.class);
//                                    intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
//                                    startActivity(intent);
                                    break;
                                }
                                //case2: if the empty document is at the last one, then we need to allow the user
                                // to submit ticket!
                                if(map.size() == 0){
                                    if(count == sizeDoc){
                                        flag[0] = true;
                                        System.out.println("case2, empty field is the last one: " + flag[0]);
//                                        Intent intent = new Intent(getApplicationContext(), SubmitTicketActivity.class);
//                                        intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
//                                        startActivity(intent);
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

//                                        Intent intent = new Intent(getApplicationContext(), SubmitTicketActivity.class);
//                                        intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
//                                        startActivity(intent);
                                        break;
                                    }else{
                                        flag[0] = false;
//                                        Toast.makeText(getApplicationContext(), "Sorry, you can't submit ticket again!", Toast.LENGTH_SHORT).show();
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
//                                    Toast.makeText(getApplicationContext(), "Sorry, you can't submit ticket again!", Toast.LENGTH_SHORT).show();
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
                                //这和我写的acceptTicketActivity的case2是一个意思!
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

//                                    Intent intent = new Intent(getApplicationContext(), SubmitTicketActivity.class);
//                                    intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
//                                    startActivity(intent);
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
//                                        Intent intent = new Intent(getApplicationContext(), SubmitTicketActivity.class);
//                                        intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
//                                        startActivity(intent);
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
//                                        Intent intent = new Intent(getApplicationContext(), SubmitTicketActivity.class);
//                                        intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
//                                        startActivity(intent);
                                        break;
                                    }else{
                                        flag[0] = false;
                                        System.out.println("the user has already had a meeting: " + document.get("student_id"));
//                                        Toast.makeText(getApplicationContext(), "Sorry, you can't submit ticket again!", Toast.LENGTH_SHORT).show();
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
//                                    Toast.makeText(getApplicationContext(), "Sorry, you can't submit ticket again!", Toast.LENGTH_SHORT).show();
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

                System.out.println("after traversing both two collections, flag is: "+ flag[0]);
                if(flag[0] == true){
                    Intent intent = new Intent(getApplicationContext(), SubmitTicketActivity.class);
                    intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "Sorry, you can't submit ticket again!", Toast.LENGTH_SHORT).show();
                }

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
                //我需要从firebase中的每个ticket中查找是否有这个userId
//                Query queryTickets;
//                Query queryMeetings;
//                queryTickets = fStore.collection("student_ticket").whereEqualTo("user id", userId);
//                queryMeetings = fStore.collection("meetings").whereEqualTo("student_id", userId);
//                //1.if the student has a ticket in queryTickets or has a meeting in queryMeetings, then not allowed to submit ticket.
//                //2. if the student doesn't have a ticket in queryTickets and
//                if(queryTickets == null && queryMeetings== null){
//                    //submit ticket
//                    System.out.println("the user doesn't have a ticket2.0!");
//                    System.out.println("queryTickets is: " + queryTickets);
//                    System.out.println(userId);
//                    Intent intent = new Intent(getApplicationContext(), SubmitTicketActivity.class);
//                    intent.putExtra(StudentRegisterActivity.GET_USER_KEY, preIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY));
//                    startActivity(intent);
//                }else{
//                    //not allowed
////                    System.out.println("the user has already had a ticket: " + document.get("user id"));
//                    Toast.makeText(getApplicationContext(), "Sorry, you can't submit ticket again!", Toast.LENGTH_SHORT).show();
//                }


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

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.myListview.getFilter().filter(s);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
