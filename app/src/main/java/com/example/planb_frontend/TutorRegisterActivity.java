package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import planb_backend.User;

import static com.example.planb_frontend.StudentRegisterActivity.USERS_TABLE_KEY;
import static com.example.planb_frontend.StudentRegisterActivity.USER_ID_KEY;
import static com.example.planb_frontend.StudentRegisterActivity.USER_TYPE_KEY;

public class TutorRegisterActivity extends AppCompatActivity{

    //声明控件
    private Button mBtnTutorRegister;
    private Button mBtnTutorLogin;
    private EditText mEtPassword;
    private EditText mEtSchoolEmail;
    private EditText mEtPreferredName;
    private EditText mEtMajor;
    private EditText mEtGrade;
    private EditText mEtPhoneNum;

    public static final String PREFERRED_NAME_KEY = "preferred_name";
    public static final String MAJOR_KEY = "major";
    public static final String CLASS_STANDING_KEY = "class_standing";
    public static final String PHONE_NUMBER_KEY = "phone_number";

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private User newUser;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_register);


        //找到控件
        mEtPassword = findViewById(R.id.tutor_password);
        mEtSchoolEmail = findViewById(R.id.tutor_email);
        mEtPreferredName = findViewById(R.id.tutor_prefer_name);
        mEtMajor = findViewById(R.id.tutor_major);
        mEtGrade = findViewById(R.id.tutor_grade);
        mEtPhoneNum = findViewById(R.id.tutor_phone_number);
        mBtnTutorRegister = findViewById(R.id.tutor_register);
        mBtnTutorLogin = findViewById(R.id.tutor_login_link);

        mBtnTutorRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Intent intent_r = new Intent(TutorRegisterActivity.this, LoginActivity.class);
                String password = mEtPassword.getText().toString();
                String schoolEmail = mEtSchoolEmail.getText().toString();
                String preferName = mEtPreferredName.getText().toString();
                String major = mEtMajor.getText().toString();
                String grade = mEtGrade.getText().toString();
                String phoneNum = mEtPhoneNum.getText().toString();

                fAuth = FirebaseAuth.getInstance();
                fStore = FirebaseFirestore.getInstance();

                Toast toast = null;

                if(TextUtils.isEmpty(schoolEmail)){
                    toast = Toast.makeText(TutorRegisterActivity.this,
                            "Please enter school email", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else if(TextUtils.isEmpty(password)){
                    toast = Toast.makeText(TutorRegisterActivity.this,
                            "Please enter password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else if(TextUtils.isEmpty(preferName)){
                    toast = Toast.makeText(TutorRegisterActivity.this,
                            "Please enter preferred name", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else if(TextUtils.isEmpty(major)){
                    toast = Toast.makeText(TutorRegisterActivity.this,
                            "Please enter major", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else if(TextUtils.isEmpty(grade)){
                    toast = Toast.makeText(TutorRegisterActivity.this,
                            "Please enter grade", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else if(TextUtils.isEmpty(phoneNum)){
                    toast = Toast.makeText(TutorRegisterActivity.this,
                            "Please enter phone number", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else{
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
                                        user.put(USER_TYPE_KEY,"tutor");
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
                                        newUser.setType("tutor");
                                        newUser.setId(userId);
                                        Intent intent = new Intent(getApplicationContext(), TutorPageActivity.class);
                                        intent.putExtra(StudentRegisterActivity.GET_USER_KEY,newUser);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Failing", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );

                }
            }
        });
        mBtnTutorLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent_l = new Intent(TutorRegisterActivity.this, LoginActivity.class);
                startActivity(intent_l);
            }
        });

    }

}
