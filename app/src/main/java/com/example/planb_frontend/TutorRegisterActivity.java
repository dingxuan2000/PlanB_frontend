package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
                Intent intent_r = new Intent(TutorRegisterActivity.this, LoginActivity.class);
                String password = mEtPassword.getText().toString();
                String schoolEmail = mEtSchoolEmail.getText().toString();
                String preferName = mEtPreferredName.getText().toString();
                String major = mEtMajor.getText().toString();
                String grade = mEtGrade.getText().toString();
                String phoneNum = mEtPhoneNum.getText().toString();

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

                    intent_r.putExtra("email",schoolEmail);
                    intent_r.putExtra("password",password);
                    startActivity(intent_r);
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
