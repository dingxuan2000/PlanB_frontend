package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    //声明控件
    private Button mBtnLogin;
    private EditText mEtUser;
    private EditText mEtPassword;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_login);

        //找到控件
        mBtnLogin = findViewById(R.id.btn_login);
        mEtUser = findViewById(R.id.username_textInput);
        mEtPassword = findViewById(R.id.password_textInput);


        //Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            password = extras.getString("password");
            mEtUser.setText(email, TextView.BufferType.EDITABLE);
            mEtPassword.setText(password, TextView.BufferType.EDITABLE);
        }


        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_n = null;
                email = mEtUser.getText().toString();
                password = mEtPassword.getText().toString();

                if(email.equals("asdf") && password.equals("123456")){
                    //如果正确，进行跳转
                    intent_n = new Intent(LoginActivity.this, StudentPageActivity.class);
                    startActivity(intent_n);
                }else{
                    //如果不正确，可能也要跳转，所以intent定义在外面，
                    //或弹出登录失败toast应用！！(需要学习)
                }
            }
        });

    }
}


