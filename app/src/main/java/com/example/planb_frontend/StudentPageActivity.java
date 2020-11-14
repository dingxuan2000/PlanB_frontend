package com.example.planb_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentPageActivity extends AppCompatActivity {

    private ImageView connectionB;
    private ImageView historyB;
    private ImageView profileB;

    private TextView textViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_student);

        connectionB = findViewById(R.id.tutor_connection_btn);
        connectionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentPageActivity.this, StudentConnectionActivity.class);
                startActivity(intent);
            }
        });

        historyB = findViewById(R.id.student_history_btn);
        historyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentPageActivity.this, StudentHistoryActivity.class);
                startActivity(intent);
            }
        });

        profileB = findViewById(R.id.student_profile_btn);
        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentPageActivity.this, StudentProfileActivity.class);
                startActivity(intent);
            }
        });

//        textViewResult = findViewById(R.id.text_view_result);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://localhost:8080/")  //192.168.1.147
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
//
//        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();
//
//        call.enqueue(new Callback<List<Post>>() {
//            @Override
//            public void onResponse(Call<List<Post>> call, Response<List<Post>> response){
//
//                if(!response.isSuccessful()){
//                    textViewResult.setText("Code: +" + response.code());
//                    return;
//                }
//
//                List<Post> posts = response.body();
//
//                for(Post post : posts){
//                    String content="";
//                    content += "password" + post.getPassword() + "\n";
//                    content += "username" + post.getPassword() + "\n";
//
//                    textViewResult.append(content);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Post>> call, Throwable t){
//                textViewResult.setText(t.getMessage());
//            }
//        } );
    }
}
