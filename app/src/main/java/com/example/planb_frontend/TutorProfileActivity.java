package com.example.planb_frontend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planb_backend.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.planb_frontend.StudentRegisterActivity.USERS_TABLE_KEY;

public class TutorProfileActivity extends AppCompatActivity {
    public static final String PREFERRED_COURSES_KEY = "preferred_courses";
    public static final String COURSE_1_KEY = "course_1";
    public static final String COURSE_2_KEY = "course_2";
    public static final String COURSE_3_KEY = "course_3";
    public static final String COURSE_4_KEY = "course_4";
    public static final String RATING_KEY = "rating";

    private ImageView connectionB;
    private ImageView historyB;
    private ImageView mainB;
    private ImageView logout;
    private ImageView courseadd;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    private TextView studentName;
    private TextView gradeStanding;
    private TextView major;
    private TextView phoneNumber;
    private TextView email;
    private TextView rating;

    private int courseCount = 0;

    private Button courseOne;
    private Button courseTwo;
    private Button courseThree;
    private Button courseFour;
    private Button confirm;

    private ImageView deleteIconOne;
    private ImageView deleteIconTwo;
    private ImageView deleteIconThree;
    private ImageView deleteIconFour;

    private Spinner spinner;

    private ArrayList<String> fourCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_tutor);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fourCourses = new ArrayList<>();

        Intent intent = getIntent();
        User passUser = (User) intent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);

        spinner = findViewById(R.id.search_spinner);

        courseOne = findViewById(R.id.course_1);
        courseTwo = findViewById(R.id.course_2);
        courseThree = findViewById(R.id.course_3);
        courseFour = findViewById(R.id.course_4);
        confirm = findViewById(R.id.confirm);

        deleteIconOne = findViewById(R.id.close_icon_1);
        deleteIconTwo = findViewById(R.id.close_icon_2);
        deleteIconThree = findViewById(R.id.close_icon_3);
        deleteIconFour = findViewById(R.id.close_icon_4);

        DocumentReference tutorDocument = fStore.collection(PREFERRED_COURSES_KEY).document(passUser.getId());
        tutorDocument.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if(!documentSnapshot.getString(COURSE_1_KEY).isEmpty()){
                        fourCourses.add(documentSnapshot.getString(COURSE_1_KEY));
                        courseOne.setText(fourCourses.get(0));
                        courseOne.setVisibility(View.VISIBLE);
                        deleteIconOne.setVisibility(View.VISIBLE);
                        courseCount++;
                    }
                    if(!documentSnapshot.getString(COURSE_2_KEY).isEmpty()){
                        fourCourses.add(documentSnapshot.getString(COURSE_2_KEY));
                        courseTwo.setText(fourCourses.get(1));
                        courseTwo.setVisibility(View.VISIBLE);
                        deleteIconTwo.setVisibility(View.VISIBLE);
                        courseCount++;
                    }
                    if(!documentSnapshot.getString(COURSE_3_KEY).isEmpty()){
                        fourCourses.add(documentSnapshot.getString(COURSE_3_KEY));
                        courseThree.setText(fourCourses.get(2));
                        courseThree.setVisibility(View.VISIBLE);
                        deleteIconThree.setVisibility(View.VISIBLE);
                        courseCount++;
                    }
                    if(!documentSnapshot.getString(COURSE_4_KEY).isEmpty()){
                        fourCourses.add(documentSnapshot.getString(COURSE_1_KEY));
                        courseFour.setText(fourCourses.get(3));
                        courseFour.setVisibility(View.VISIBLE);
                        deleteIconFour.setVisibility(View.VISIBLE);
                        courseCount++;
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Document does not exists.", Toast.LENGTH_LONG).show();
                }
            }
        });

        ArrayAdapter<CharSequence> courseStringAdapter = ArrayAdapter.createFromResource(this, R.array.courses, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(courseStringAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter course code", Toast.LENGTH_SHORT).show();
                } else {
                    if (courseCount >= 4) {
                        //todo:set reminder
                        TextView errorText = (TextView)spinner.getSelectedView();
                        errorText.setError("");
                        errorText.setTextColor(Color.RED);
                        errorText.setText("Pick at most four courses");
                        Toast.makeText(getApplicationContext(), "  course count: " + courseCount, Toast.LENGTH_SHORT).show();
                    } else {
                        String courseSelected = adapterView.getItemAtPosition(i).toString().trim();
                        if (fourCourses.contains(courseSelected)) {
                            //todo:set reminder, already picked xxx course
                            TextView errorText = (TextView)spinner.getSelectedView();
                            errorText.setError("");
                            errorText.setTextColor(Color.RED);
                            errorText.setText("Duplicated with "+ courseSelected);
                        } else {
                            fourCourses.add(courseSelected);
                            courseCount++;
                            resetText();
                            Toast.makeText(getApplicationContext(), courseSelected + " added" + courseCount + " list size: "+ fourCourses.size(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rating = findViewById(R.id.rating);
        DocumentReference tutorRating = fStore.collection(USERS_TABLE_KEY).document(passUser.getId());
        tutorRating.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    rating.setText("Rating: " + documentSnapshot.getLong(RATING_KEY).toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Document does not exists.", Toast.LENGTH_LONG).show();
                }
            }
        });

        studentName = findViewById(R.id.tutor_name);
        studentName.setText(passUser.getPreferred_name());
        gradeStanding = findViewById(R.id.grade_standing);
        gradeStanding.setText(passUser.getClass_standing());
        major = findViewById(R.id.major);
        major.setText(passUser.getMajor());
        phoneNumber = findViewById(R.id.phone_number);
        phoneNumber.setText(passUser.getPhone_number());
        email = findViewById(R.id.email);
        email.setText(passUser.getEmail());

//        courseadd = findViewById(R.id.courseadd);
//        courseadd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent previousIntent = getIntent();
//                User addCourseUser = (User) previousIntent.getSerializableExtra(StudentRegisterActivity.GET_USER_KEY);
//                Toast.makeText(getApplicationContext(), "LEAVING PROFILE: " + addCourseUser.toString(), Toast.LENGTH_LONG).show();
//                Intent addCourse = new Intent(TutorProfileActivity.this, AddCourses.class);
//                intent.putExtra(StudentRegisterActivity.GET_USER_KEY, addCourseUser);
//                startActivity(addCourse);
//            }
//        });

        connectionB = findViewById(R.id.student_connection_btn);
        connectionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorProfileActivity.this, TutorConnectionActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY, passUser);
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.student_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorProfileActivity.this, TutorHistoryActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY, passUser);
                startActivity(intent);
            }
        });

        mainB = findViewById(R.id.student_main_btn);
        mainB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorProfileActivity.this, TutorPageActivity.class);
                intent.putExtra(StudentRegisterActivity.GET_USER_KEY, passUser);
                startActivity(intent);
            }
        });

        deleteIconOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findAndRemoveCourse((String) courseOne.getText());
                resetText();
                courseCount--;
            }
        });
        deleteIconTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findAndRemoveCourse((String) courseTwo.getText());
                resetText();
                courseCount--;
            }
        });
        deleteIconThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findAndRemoveCourse((String) courseThree.getText());
                resetText();
                courseCount--;
            }
        });
        deleteIconFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findAndRemoveCourse((String) courseFour.getText());
                resetText();
                courseCount--;
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillEmpty();
                String userId = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection(PREFERRED_COURSES_KEY).document(userId);
                Map<String, Object> courses = new HashMap<>();
                courses.put(COURSE_1_KEY, fourCourses.get(0));
                courses.put(COURSE_2_KEY, fourCourses.get(1));
                courses.put(COURSE_3_KEY, fourCourses.get(2));
                courses.put(COURSE_4_KEY, fourCourses.get(3));
                documentReference.set(courses).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Change Successful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TutorProfileActivity.this);
                builder.setIcon(R.drawable.warning);
                builder.setTitle("Sign Out");
                builder.setMessage("Are you sure that you want to sign out?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fAuth.signOut();
                        Intent intent = new Intent(TutorProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Successfully signed out", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(TutorProfileActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

//    private String isRepeated(String courseName) {
//        for (String s : fourCourses) {
//            if (courseName.equals(s)) {
//                return s;
//            } else {
//                return "";
//            }
//        }
//        return "";
//    }

    private void findAndRemoveCourse(String courseName){
        for (int i = 0; i < fourCourses.size(); i++) {
            if (courseName.equals(fourCourses.get(i))) {
                fourCourses.remove(i);
            }
        }
    }

    private void fillEmpty(){
        for(int i = 0; i < 4; i++){
            fourCourses.add("");
        }
    }

    private void resetText(){
        courseOne.setVisibility(View.INVISIBLE);
        courseTwo.setVisibility(View.INVISIBLE);
        courseThree.setVisibility(View.INVISIBLE);
        courseFour.setVisibility(View.INVISIBLE);
        deleteIconOne.setVisibility(View.INVISIBLE);
        deleteIconTwo.setVisibility(View.INVISIBLE);
        deleteIconThree.setVisibility(View.INVISIBLE);
        deleteIconFour.setVisibility(View.INVISIBLE);

        for(int i = 0; i < fourCourses.size(); i++){
            if(i == 0 && !fourCourses.get(i).isEmpty()){
                courseOne.setText(fourCourses.get(i));
                courseOne.setVisibility(View.VISIBLE);
                deleteIconOne.setVisibility(View.VISIBLE);
            }
            else if(i == 1 && !fourCourses.get(i).isEmpty()){
                courseTwo.setText(fourCourses.get(i));
                courseTwo.setVisibility(View.VISIBLE);
                deleteIconTwo.setVisibility(View.VISIBLE);
            }
            else if(i == 2 && !fourCourses.get(i).isEmpty()){
                courseThree.setText(fourCourses.get(i));
                courseThree.setVisibility(View.VISIBLE);
                deleteIconThree.setVisibility(View.VISIBLE);
            }
            else if(i == 3 && !fourCourses.get(i).isEmpty()){
                courseFour.setText(fourCourses.get(i));
                courseFour.setVisibility(View.VISIBLE);
                deleteIconFour.setVisibility(View.VISIBLE);
            }
        }
    }
//    private void addText(String courseToDisplay){
//        if(fourCourses.size() == 1){
//            courseOne.setText(fourCourses.get(0));
//            courseOne.setVisibility(View.VISIBLE);
//            deleteIconOne.setVisibility(View.VISIBLE);
//        }
//        else if(fourCourses.size() == 2){
//            courseTwo.setText(fourCourses.get(1));
//            courseTwo.setVisibility(View.VISIBLE);
//            deleteIconTwo.setVisibility(View.VISIBLE);
//            Toast.makeText(getApplicationContext(),"SIZE IS TWO",Toast.LENGTH_SHORT);
//        }
//        else if(fourCourses.size() == 3){
//            courseThree.setText(fourCourses.get(2));
//            courseThree.setVisibility(View.VISIBLE);
//            deleteIconThree.setVisibility(View.VISIBLE);
//            Toast.makeText(getApplicationContext(),"SIZE IS THREE",Toast.LENGTH_SHORT);
//        }
//        else if(fourCourses.size() == 4){
//            courseFour.setText(fourCourses.get(3));
//            courseFour.setVisibility(View.VISIBLE);
//            deleteIconFour.setVisibility(View.VISIBLE);
//        }
//    }
}







