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

public class StudentRegisterActivity extends AppCompatActivity{

    //声明控件
    private Button mBtnStuRegister;
    private Button mBtnStuLogin;
    private EditText mEtPassword;
    private EditText mEtSchoolEmail;
    private EditText mEtPreferredName;
    private EditText mEtMajor;
    private EditText mEtGrade;
    private EditText mEtPhoneNum;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_register);

        //找到控件
        mEtPassword = findViewById(R.id.stu_password);
        mEtSchoolEmail = findViewById(R.id.stu_email);
        mEtPreferredName = findViewById(R.id.stu_prefer_name);
        mEtMajor = findViewById(R.id.stu_major);
        mEtGrade = findViewById(R.id.stu_grade);
        mEtPhoneNum = findViewById(R.id.stu_phone_number);
        mBtnStuRegister = findViewById(R.id.stu_register);
        mBtnStuLogin = findViewById(R.id.student_login_link);


        mBtnStuRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent_r = new Intent(StudentRegisterActivity.this, LoginActivity.class);
                String password = mEtPassword.getText().toString();
                String schoolEmail = mEtSchoolEmail.getText().toString();
                String preferName = mEtPreferredName.getText().toString();
                String major = mEtMajor.getText().toString();
                String grade = mEtGrade.getText().toString();
                String phoneNum = mEtPhoneNum.getText().toString();
                Toast toast = null;

                if(TextUtils.isEmpty(schoolEmail)){
                    toast = Toast.makeText(StudentRegisterActivity.this,
                            "Please enter school email", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else if(TextUtils.isEmpty(password)){
                    toast = Toast.makeText(StudentRegisterActivity.this,
                            "Please enter password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else if(TextUtils.isEmpty(preferName)){
                    toast = Toast.makeText(StudentRegisterActivity.this,
                            "Please enter preferred name", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else if(TextUtils.isEmpty(major)){
                    toast = Toast.makeText(StudentRegisterActivity.this,
                            "Please enter major", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else if(TextUtils.isEmpty(grade)){
                    toast = Toast.makeText(StudentRegisterActivity.this,
                            "Please enter grade", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else if(TextUtils.isEmpty(phoneNum)){
                    toast = Toast.makeText(StudentRegisterActivity.this,
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
        mBtnStuLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent_l = new Intent(StudentRegisterActivity.this, LoginActivity.class);
                startActivity(intent_l);
            }
        });
    }


//    private void onClick(View v){
//        String preName = mEtPreferredName.getText().toString();
//        String schoolEmail = mEtSchoolEmail.getText().toString();
//        String password = mEtPassword.getText().toString();
//        String phoneNum = mEtPhoneNum.getText().toString();
//        Intent intent = null;
//
//        if(TextUtils.isEmpty(preName)){
//            Toast.makeText(com.example.planb_frontend.StudentRegisterActivity.this,
//                    "Please enter preferred name", Toast.LENGTH_LONG).show();
//        }
//        else if(TextUtils.isEmpty(schoolEmail)){
//            Toast.makeText(com.example.planb_frontend.StudentRegisterActivity.this,
//                    "Please enter school email", Toast.LENGTH_LONG).show();
//        }
//        else if(TextUtils.isEmpty(password)){
//            Toast.makeText(com.example.planb_frontend.StudentRegisterActivity.this,
//                    "Please enter password", Toast.LENGTH_LONG).show();
//        }
//        else if(TextUtils.isEmpty(phoneNum)){
//            Toast.makeText(com.example.planb_frontend.StudentRegisterActivity.this,
//                    "Please enter phone number", Toast.LENGTH_LONG).show();
//        }
//        else{
//            intent = new Intent(com.example.planb_frontend.StudentRegisterActivity.this, StudentPageActivity.class);
//            startActivity(intent);
//        }
//
//    }
}
