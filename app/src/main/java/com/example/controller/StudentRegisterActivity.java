package com.example.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import com.example.model.User;

public class StudentRegisterActivity extends AppCompatActivity {

    public static final String PREFERRED_NAME_KEY = "preferred_name";
    public static final String MAJOR_KEY = "major";
    public static final String CLASS_STANDING_KEY = "class_standing";
    public static final String PHONE_NUMBER_KEY = "phone_number";
    public static final String USERS_TABLE_KEY = "users";
    public static final String GET_USER_KEY = "get user";
    public static final String USER_TYPE_KEY = "type";
    public static final String USER_ID_KEY = "user id";

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    //声明控件
    private Button mBtnStuRegister;
    private EditText mEtPassword;
    private EditText mEtSchoolEmail;
    private EditText mEtPreferredName;
    private Spinner mEtMajor;
    private Spinner mEtGrade;
    private EditText mEtPhoneNum;

    private Button login;

    private String major;
    private String grade;

    private User newUser;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_register);

        login = findViewById(R.id.student_login_link);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentRegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //找到控件
        mEtPassword = findViewById(R.id.stu_password);
        mEtSchoolEmail = findViewById(R.id.stu_email);
        mEtPreferredName = findViewById(R.id.stu_prefer_name);
        mEtMajor = findViewById(R.id.stu_major);
        mEtGrade = findViewById(R.id.stu_grade);
        mEtPhoneNum = findViewById(R.id.stu_phone_number);
        mBtnStuRegister = findViewById(R.id.stu_register);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.majors, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEtMajor.setAdapter(adapter);
        mEtMajor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                major = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mEtMajor.setPrompt("Please enter major");
            }
        });

//        ArrayAdapter<CharSequence> adapter_grade = ArrayAdapter.createFromResource(this, R.array.class_standing, R.layout.style_spinner1);
//        adapter.setDropDownViewResource(R.layout.custom_spinner_layout);

        ArrayAdapter<CharSequence> adapter_grade = ArrayAdapter.createFromResource(this, R.array.class_standing, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEtGrade.setAdapter(adapter_grade);
        mEtGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                grade = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mEtGrade.setPrompt("Choose Your Grade");
            }
        });


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        mBtnStuRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mEtPassword.getText().toString();
                String schoolEmail = mEtSchoolEmail.getText().toString();
                String preferName = mEtPreferredName.getText().toString();
                String phoneNum = mEtPhoneNum.getText().toString();


                if (TextUtils.isEmpty(schoolEmail)) {
                    mEtSchoolEmail.setError("Please enter school email");
                } else if (TextUtils.isEmpty(password)) {
                    mEtPassword.setError("Please enter password");
                } else if (TextUtils.isEmpty(preferName)) {
                    mEtPreferredName.setError("Please enter preferred name");
                }
                else if (TextUtils.isEmpty(phoneNum)) {
                    mEtPhoneNum.setError("Please enter phone number");
                } else {
                    //TODO update to firebase
                    fAuth.createUserWithEmailAndPassword(schoolEmail, password).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //get user id just generated
                                        String userId = fAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference = fStore.collection(USERS_TABLE_KEY).document(userId);
                                        Map<String, Object> user = new HashMap<>();
                                        user.put(PREFERRED_NAME_KEY, preferName);
                                        user.put(MAJOR_KEY, major);
                                        user.put(CLASS_STANDING_KEY, grade);
                                        user.put(PHONE_NUMBER_KEY, phoneNum);
                                        user.put(USER_TYPE_KEY,"student");
                                        user.put(USER_ID_KEY,userId);
                                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        newUser = new User();
                                        newUser.setEmail(schoolEmail);
                                        newUser.setClass_standing(grade);
                                        newUser.setMajor(major);
                                        newUser.setPhone_number(phoneNum);
                                        newUser.setPreferred_name(preferName);
                                        newUser.setType("student");
                                        newUser.setId(userId);
                                        Intent intent = new Intent(getApplicationContext(), StudentPageActivity.class);
                                        intent.putExtra(GET_USER_KEY,newUser);
                                        startActivity(intent);
                                    } else {
                                        Log.e("tag", task.getException().toString());
                                        Toast.makeText(getApplicationContext(), "Failing", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );

                }
            }
        });
    }
}
