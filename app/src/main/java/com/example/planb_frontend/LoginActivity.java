package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URLEncoder;

import com.example.planb_backend.User;

import org.w3c.dom.Text;

import static com.example.planb_frontend.StudentRegisterActivity.USERS_TABLE_KEY;


public class LoginActivity extends AppCompatActivity {

    public static final String USER_INFO_TAG = "user info";
    //声明控件
    private Button mBtnLogin;
    private EditText mEtUser;
    private EditText mEtPassword;
    private String email, password;
    private TextView create;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //找到控件
        mBtnLogin = findViewById(R.id.btn_login);
        mEtUser = findViewById(R.id.username_textInput);
        mEtPassword = findViewById(R.id.password_textInput);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, getStartedActivity.class);
                startActivity(intent);
            }
        });

        if (fAuth.getCurrentUser() != null) {
            String userId = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection(USERS_TABLE_KEY).document(userId);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        user = documentSnapshot.toObject(User.class);
                        user.setEmail(fAuth.getCurrentUser().getEmail());
                        user.setId(userId);
                        if (user.getType().equals("tutor")) {
                            Intent intent = new Intent(getApplicationContext(), TutorPageActivity.class);
                            intent.putExtra(StudentRegisterActivity.GET_USER_KEY, user);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), user.toString(), Toast.LENGTH_LONG).show();
                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(), StudentPageActivity.class);
                            intent.putExtra(StudentRegisterActivity.GET_USER_KEY, user);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), user.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "error occurred", Toast.LENGTH_LONG).show();
                    }
                }
            });

            finish();
        } else {
            mBtnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_n = null;
                    email = mEtUser.getText().toString();
                    password = mEtPassword.getText().toString();

                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String userId = fAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference = fStore.collection(USERS_TABLE_KEY).document(userId);
                                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    user = documentSnapshot.toObject(User.class);
                                                    Toast.makeText(getApplicationContext(), user.toString(), Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "error occurred", Toast.LENGTH_LONG).show();
                                                }
                                                Intent intent = null;
                                                if (user.getType().equals("tutor")) {
                                                    intent = new Intent(getApplicationContext(), TutorPageActivity.class);
                                                } else {
                                                    intent = new Intent(getApplicationContext(), StudentPageActivity.class);
                                                }
                                                intent.putExtra(StudentRegisterActivity.GET_USER_KEY, user);
                                                startActivity(intent);
                                                Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                                            }
                                        });
//                                        Intent intent = null;
//                                        if (user.getType().equals("tutor")) {
//                                            intent = new Intent(getApplicationContext(), TutorPageActivity.class);
//                                        } else {
//                                            intent = new Intent(getApplicationContext(), StudentPageActivity.class);
//                                        }
//                                        intent.putExtra(StudentRegisterActivity.GET_USER_KEY, user);
//                                        startActivity(intent);
//                                        Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
                }
            });
        }

    }
}


