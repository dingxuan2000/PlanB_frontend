package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TutorProfileActivity extends AppCompatActivity {
    private ImageView connectionB;
    private ImageView historyB;
    private ImageView mainB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_tutor);

<<<<<<< Updated upstream
        connectionB = findViewById(R.id.tutor_main_btn);
=======
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
//                if (documentSnapshot.exists()) {
//                    rating.setText("Rating: " + documentSnapshot.getLong(RATING_KEY).toString());
//                } else {
//                    Toast.makeText(getApplicationContext(), "Document does not exists.", Toast.LENGTH_LONG).show();
//                }
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

        connectionB = findViewById(R.id.tutor_connection_btn);
>>>>>>> Stashed changes
        connectionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorProfileActivity.this, TutorConnectionActivity.class);
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.tutor_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorProfileActivity.this, TutorHistoryActivity.class);
                startActivity(intent);
            }
        });

        mainB = findViewById(R.id.tutor_main_btn);
        mainB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorProfileActivity.this, TutorPageActivity.class);
                startActivity(intent);
            }
        });
    }
}







