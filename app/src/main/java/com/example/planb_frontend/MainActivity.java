package com.example.planb_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run(){
                Intent mainToStart = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(mainToStart);
                MainActivity.this.finish();
            }
        }, 2000);
    }
}