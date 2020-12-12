package com.example.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;


public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;
    private ProgressBar progressBar;
    private ImageView backArrow;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        emailEditText = findViewById(R.id.email);
        resetPasswordButton = findViewById(R.id.resetPassword);
        progressBar = findViewById(R.id.progressBar);
        backArrow = findViewById(R.id.backarrow);

        auth = FirebaseAuth.getInstance();


        backArrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        resetPasswordButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               resetPassword();
//               Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
//               startActivity(intent);
               final Handler handler = new Handler();
               handler.postDelayed(new Runnable() {
                   public void run(){
                       Intent mainToStart = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                       ResetPasswordActivity.this.startActivity(mainToStart);
                       ResetPasswordActivity.this.finish();
                   }
               }, 2000);
           }
        });

    }

    private void resetPassword(){
        String email = emailEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
            //Toast.makeText(ResetPasswordActivity.this, "Check your to reset your password!", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>(){
           @Override
           public void onComplete(@NonNull Task<Void> task){

               if(task.isSuccessful()){
                   Toast.makeText(ResetPasswordActivity.this, "Check your to reset your password!", Toast.LENGTH_LONG).show();
               }else{
                   Toast.makeText(ResetPasswordActivity.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
               }
           }
        });


    }


}