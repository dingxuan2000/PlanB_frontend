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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class StudentUpcomingCustomListView extends ArrayAdapter<String> {
    
    private ArrayList<String> tutor_name;
    private ArrayList<String> course_code;
    private ArrayList<String> time;
    private ArrayList<String> phone_number;
    private ArrayList<String> meeting_id;

    private User studentUser;
    private Activity context;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();


    private String curr_meeting_id;

    public StudentUpcomingCustomListView(Activity context, ArrayList<String> tutor_name, ArrayList<String> course_code,
                                         ArrayList<String> time, ArrayList<String> phone_number, ArrayList<String> meeting_id, User studentUser) {
        super(context, R.layout.student_upcoming_listview_layout, tutor_name);

        this.context = context;
        this.tutor_name = tutor_name;
        this.course_code = course_code;
        this.time = time;
        this.phone_number = phone_number;
        this.meeting_id = meeting_id;
        this.studentUser = studentUser;
        }

        @NonNull
        @Override
        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {

            View r = convertView;
            ViewHolder viewHolder;
            if(r == null) {
                LayoutInflater layoutInflater=context.getLayoutInflater();
                r = layoutInflater.inflate(R.layout.student_upcoming_listview_layout,null,true);
                viewHolder = new ViewHolder(r);
                r.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) r.getTag();
            }

            //viewHolder.ivt.setImageResource(tutor_imgid.get(position0);
            viewHolder.tvname.setText(tutor_name.get(position));
            viewHolder.tvccode.setText(course_code.get(position));
            viewHolder.tvtime.setText(time.get(position));
            viewHolder.tvpnumber.setText(phone_number.get(position));

            //Handle buttons and add onClickListeners
            Button cancelBtn = r.findViewById(R.id.cancel_Btn);
            curr_meeting_id = meeting_id.get(position);


            cancelBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //add alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.warning);
                    builder.setTitle("Verify");
                    builder.setMessage("Are you sure you want to cancel the meeting?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fStore.collection("meetings").document(curr_meeting_id).delete();
                            Intent intent = new Intent(context, StudentConnectionActivity.class);
                            intent.putExtra(StudentRegisterActivity.GET_USER_KEY, studentUser);
                            context.startActivity(intent);
                            context.finish();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
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


            ViewHolder(View v){
                tvname = v.findViewById(R.id.student_upcoming_name);
                tvccode = v.findViewById(R.id.student_upcoming_ccode);
                tvtime = v.findViewById(R.id.student_upcoming_time);
                tvpnumber = v.findViewById(R.id.student_upcoming_pnumber);
            }
        }
}
