package com.example.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.model.User;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddCourses extends AppCompatActivity {

    public static final String PREFERRED_COURSES_KEY = "preferred_courses";
    public static final String COURSE_1_KEY = "course_1";
    public static final String COURSE_2_KEY = "course_2";
    public static final String COURSE_3_KEY = "course_3";
    public static final String COURSE_4_KEY = "course_4";
    public static final String COURSE_5_KEY = "course_5";
    private Spinner mEtCourse1;
    private Spinner mEtCourse2;
    private Spinner mEtCourse3;
    private Spinner mEtCourse4;
    private Spinner mEtCourse5;
    private Button mBtnUpdate;
    private String courses1;
    private String courses2;
    private String courses3;
    private String courses4;
    private String courses5;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_courses);

        mEtCourse1 = findViewById(R.id.course1);
        mEtCourse2 = findViewById(R.id.course2);
        mEtCourse3 = findViewById(R.id.course3);
        mEtCourse4 = findViewById(R.id.course4);
        mEtCourse5 = findViewById(R.id.course5);
        mBtnUpdate = findViewById(R.id.update);

        Intent prevIntent = getIntent();
        user = (User)prevIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);
//        Toast.makeText(getApplicationContext(),"COURSES ADD: "+user.toString(),Toast.LENGTH_LONG).show();


        ArrayAdapter<CharSequence> adapter_course1 = ArrayAdapter.createFromResource(this, R.array.courses, android.R.layout.simple_spinner_dropdown_item);
        adapter_course1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEtCourse1.setAdapter(adapter_course1);
        mEtCourse1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                courses1 = adapterView.getItemAtPosition(i).toString();
                mEtCourse1.setPrompt("Change Marked");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<CharSequence> adapter_course2 = ArrayAdapter.createFromResource(this, R.array.courses, android.R.layout.simple_spinner_dropdown_item);
        adapter_course2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEtCourse2.setAdapter(adapter_course2);
        mEtCourse2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                courses2 = adapterView.getItemAtPosition(i).toString();
                mEtCourse2.setPrompt("Change Marked");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<CharSequence> adapter_course3 = ArrayAdapter.createFromResource(this, R.array.courses, android.R.layout.simple_spinner_dropdown_item);
        adapter_course3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEtCourse3.setAdapter(adapter_course3);
        mEtCourse3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                courses3 = adapterView.getItemAtPosition(i).toString();
                mEtCourse3.setPrompt("Change Marked");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<CharSequence> adapter_course4 = ArrayAdapter.createFromResource(this, R.array.courses, android.R.layout.simple_spinner_dropdown_item);
        adapter_course4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEtCourse4.setAdapter(adapter_course4);
        mEtCourse4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                courses4 = adapterView.getItemAtPosition(i).toString();
                mEtCourse4.setPrompt("Change Marked");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<CharSequence> adapter_course5 = ArrayAdapter.createFromResource(this, R.array.courses, android.R.layout.simple_spinner_dropdown_item);
        adapter_course5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEtCourse5.setAdapter(adapter_course5);
        mEtCourse5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                courses5 = adapterView.getItemAtPosition(i).toString();
                mEtCourse5.setPrompt("Change Marked");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update courses to firebase
                fAuth = FirebaseAuth.getInstance();
                fStore = FirebaseFirestore.getInstance();
                String userId = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection(PREFERRED_COURSES_KEY).document(userId);
                Map<String, Object> courses = new HashMap<>();
                courses.put(COURSE_1_KEY, courses1);
                courses.put(COURSE_2_KEY, courses2);
                courses.put(COURSE_3_KEY, courses3);
                courses.put(COURSE_4_KEY, courses4);
                courses.put(COURSE_5_KEY,courses5);
                courses.put(TutorPageActivity.TUTOR_STATUS_KEY,"offCampus");
                documentReference.set(courses).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Change Successful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
