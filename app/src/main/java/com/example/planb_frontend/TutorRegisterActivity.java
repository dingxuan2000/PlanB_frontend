package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
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

import com.example.planb_backend.User;

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
    private Spinner mEtMajor;
    private Spinner mEtGrade;
    private EditText mEtPhoneNum;

    private String major;
    private String grade;

    public static final String PREFERRED_NAME_KEY = "preferred_name";
    public static final String MAJOR_KEY = "major";
    public static final String CLASS_STANDING_KEY = "class_standing";
    public static final String PHONE_NUMBER_KEY = "phone_number";
    public static final String USERS_TABLE_KEY = "users";

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


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

        ArrayAdapter<CharSequence> adapter_major = ArrayAdapter.createFromResource(this, R.array.majors, android.R.layout.simple_spinner_dropdown_item);
        adapter_major.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEtMajor.setAdapter(adapter_major);
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

        ArrayAdapter<CharSequence> adapter_grade = ArrayAdapter.createFromResource(this, R.array.class_standing, android.R.layout.simple_spinner_dropdown_item);
        adapter_grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEtGrade.setAdapter(adapter_grade);
        mEtGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                grade = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mEtGrade.setPrompt("Please enter grade");
            }
        });

        mBtnTutorRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Intent intent_r = new Intent(TutorRegisterActivity.this, LoginActivity.class);
                String password = mEtPassword.getText().toString();
                String schoolEmail = mEtSchoolEmail.getText().toString();
                String preferName = mEtPreferredName.getText().toString();
                //String major = mEtMajor.getText().toString();
                //String grade = mEtGrade.getText().toString();
                String phoneNum = mEtPhoneNum.getText().toString();


                fAuth = FirebaseAuth.getInstance();
                fStore = FirebaseFirestore.getInstance();


                if (TextUtils.isEmpty(schoolEmail)) {
                    mEtSchoolEmail.setError("Please enter school email");
                } else if (TextUtils.isEmpty(password)) {
                    mEtPassword.setError("Please enter password");
                } else if (TextUtils.isEmpty(preferName)) {
                    mEtPreferredName.setError("Please enter preferred name");
                }
//                else if(TextUtils.isEmpty(major)){
//                    mEtMajor.setError("Please enter major");
//                }
//                else if (TextUtils.isEmpty(grade)) {
//                    mEtGrade.setError("Please enter grade");
//                }
                else if (TextUtils.isEmpty(phoneNum)) {
                    mEtPhoneNum.setError("Please enter phone number");
                } else{
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
                                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        Intent intent = new Intent(getApplicationContext(), TutorPageActivity.class);
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

        mBtnTutorLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorRegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

}
