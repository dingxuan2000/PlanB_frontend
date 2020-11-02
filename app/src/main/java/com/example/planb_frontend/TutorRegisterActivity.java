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
    private EditText mEtPreferredName;
    private EditText mEtPassword;
    private EditText mEtSchoolEmail;
    private EditText mEtPhoneNum;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_register);

        //找到控件
        mBtnTutorRegister = findViewById(R.id.register_tutor);
        mEtSchoolEmail = findViewById(R.id.school_name);
        mEtPreferredName = findViewById(R.id.preferred_name);
        mEtPassword = findViewById(R.id.password);
        mEtPhoneNum = findViewById(R.id.phone_number);
        mBtnTutorRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.planb_frontend.TutorRegisterActivity.this, StudentPageActivity.class);
                String preName = mEtPreferredName.getText().toString();
                String schoolEmail = mEtSchoolEmail.getText().toString();
                String password = mEtPassword.getText().toString();
                String phoneNum = mEtPhoneNum.getText().toString();

                Toast toast = null;

                if(TextUtils.isEmpty(preName)){
                    toast = Toast.makeText(com.example.planb_frontend.TutorRegisterActivity.this,
                            "Please enter preferred name", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else if(TextUtils.isEmpty(schoolEmail)){
                    toast = Toast.makeText(com.example.planb_frontend.TutorRegisterActivity.this,
                            "Please enter school email", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else if(TextUtils.isEmpty(password)){
                    toast = Toast.makeText(com.example.planb_frontend.TutorRegisterActivity.this,
                            "Please enter password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else if(TextUtils.isEmpty(phoneNum)){
                    toast = Toast.makeText(com.example.planb_frontend.TutorRegisterActivity.this,
                            "Please enter phone number", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }
                else{
                    startActivity(intent);
                }
            }
        });
    }

}
