package com.example.planb_frontend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TutorConnectionActivity extends AppCompatActivity {

    private ImageView mainB;
    private ImageView historyB;
    private ImageView profileB;
    private LinearLayout meet;
    private LinearLayout upcoming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_tutor);

        mainB = findViewById(R.id.tutor_main_btn);
        mainB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorConnectionActivity.this, TutorPageActivity.class);
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.tutor_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorConnectionActivity.this, TutorHistoryActivity.class);
                startActivity(intent);
            }
        });

        profileB = findViewById(R.id.tutor_profile_btn);
        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorConnectionActivity.this, TutorProfileActivity.class);
                startActivity(intent);
            }
        });

        //Meet verify box
        meet = findViewById(R.id.meet);
        meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(TutorProfileActivity.this, LoginActivity.class);
//                startActivity(intent);
                AlertDialog.Builder builder = new AlertDialog.Builder(TutorConnectionActivity.this);
                builder.setIcon(R.drawable.warning);
                builder.setTitle("Verify");
                builder.setMessage("Are you sure that you finish the meeting?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Intent intent = new Intent(TutorConnectionActivity.this, StudentHistoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Toast.makeText(TutorConnectionActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        //Upcoming verify box
        upcoming = findViewById(R.id.upcoming);
        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(TutorProfileActivity.this, LoginActivity.class);
//                startActivity(intent);
                AlertDialog.Builder builder = new AlertDialog.Builder(TutorConnectionActivity.this);
                builder.setIcon(R.drawable.warning);
                builder.setTitle("Verify");
                builder.setMessage("Are you sure that you meet the student?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Intent intent = new Intent(TutorConnectionActivity.this, TutorConnectionActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Toast.makeText(TutorConnectionActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



    }
}


