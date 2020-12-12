package com.example.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TutorUpcomingCustomListView extends ArrayAdapter<String> {

    private ArrayList<String> student_name;
    private ArrayList<String> course_code;
    private ArrayList<String> time;
    private ArrayList<String> student_phone_number;
    private ArrayList<String> meeting_id;
    private User tutor;
    private String curr_meeting_id;

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    private Activity context;

    public TutorUpcomingCustomListView(Activity context, ArrayList<String> student_name, ArrayList<String> course_code,
                                       ArrayList<String> time, ArrayList<String> student_phone_number, ArrayList<String> meeting_id, User tutor) {
        super(context, R.layout.tutor_upcoming_listview_layout, student_name);

        this.context = context;
        this.student_name = student_name;
        this.course_code = course_code;
        this.time = time;
        this.student_phone_number = student_phone_number;
        this.meeting_id = meeting_id;
        this.tutor = tutor;

    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder;
        if(r == null) {
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.tutor_upcoming_listview_layout,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder.tvname.setText(student_name.get(position));
        viewHolder.tvccode.setText(course_code.get(position));
        viewHolder.tvtime.setText(time.get(position));
        viewHolder.tvpnumber.setText(student_phone_number.get(position));


        //Handle buttons and add onClickListeners
        Button startBtn = r.findViewById(R.id.start_Btn);
        Button cancelBtn = r.findViewById(R.id.cancel_Btn);

        curr_meeting_id = meeting_id.get(position);

        startBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.warning);
                builder.setTitle("Verify");
                builder.setMessage("Are you sure that the meeting has started?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fStore.collection("meetings").document(curr_meeting_id).update("status", "Ongoing");
                        Intent intent = new Intent(context, TutorConnectionActivity.class);
                        intent.putExtra(StudentRegisterActivity.GET_USER_KEY, tutor);
                        context.startActivity(intent);
                        context.finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog1 = builder.create();
                dialog1.show();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.warning);
                builder.setTitle("Verify");
                builder.setMessage("Are you sure you want to cancel the meeting?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fStore.collection("meetings").document(curr_meeting_id).delete();
                        Intent intent = new Intent(context, TutorConnectionActivity.class);
                        intent.putExtra(StudentRegisterActivity.GET_USER_KEY, tutor);
                        context.startActivity(intent);
                        context.finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog1 = builder.create();
                dialog1.show();


            }
        });


        return r;
    }

    class ViewHolder{
        TextView tvname;
        TextView tvccode;
        TextView tvtime;
        TextView tvpnumber;
        //ImageView ivt;
        ViewHolder(View v){
            tvname = v.findViewById(R.id.tutor_connection_name);
            tvccode = v.findViewById(R.id.tutor_connection_ccode);
            tvtime = v.findViewById(R.id.tutor_connection_time);
            tvpnumber = v.findViewById(R.id.tutor_conncetion_pnumber);
        }
    }
}
