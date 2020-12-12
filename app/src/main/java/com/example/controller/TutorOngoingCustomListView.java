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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TutorOngoingCustomListView extends ArrayAdapter<String> {

    private ArrayList<String> student_name;
    private ArrayList<String> course_code;
    private ArrayList<String> time;
    private ArrayList<String> student_phone_number;
    private ArrayList<String> meeting_id;
    private ArrayList<String> student_id;
    private User tutor;
    private String curr_meeting_id;
    private String curr_student_id;
    private String curr_tutor_id;
    private String curr_course;
    private String curr_time_period;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    private Activity context;


    public TutorOngoingCustomListView(Activity context, ArrayList<String> student_name, ArrayList<String> course_code,
                                      ArrayList<String> time, ArrayList<String> student_phone_number, ArrayList<String> meeting_id,
                                      ArrayList<String> student_id, User tutor) {
        super(context, R.layout.tutor_ongoing_listview_layout, student_name);
        this.context = context;
        this.student_name = student_name;
        this.course_code = course_code;
        this.time = time;
        this.student_phone_number = student_phone_number;
        this.meeting_id = meeting_id;
        this.tutor = tutor;
        this.student_id = student_id;
    }

        @NonNull
        @Override
        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {

            View r = convertView;
            ViewHolder viewHolder;
            if(r == null) {
                LayoutInflater layoutInflater=context.getLayoutInflater();
                r = layoutInflater.inflate(R.layout.tutor_ongoing_listview_layout,null,true);
                viewHolder = new ViewHolder(r);
                r.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) r.getTag();
            }


            //initialize variable
            curr_meeting_id = meeting_id.get(position);
            curr_student_id = student_id.get(position);
            curr_tutor_id = tutor.getId();
            curr_student_id = student_id.get(position);
            curr_course = course_code.get(position);
            curr_time_period = time.get(position);


            viewHolder.tvname.setText(student_name.get(position));
            viewHolder.tvccode.setText(curr_course);
            viewHolder.tvtime.setText(curr_time_period);
            viewHolder.tvpnumber.setText(student_phone_number.get(position));

            //Handle buttons and add onClickListeners
            Button endBtn = r.findViewById(R.id.end_Btn);



            endBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.warning);
                    builder.setTitle("Verify");
                    builder.setMessage("Are you sure that the meeting has ended?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Task: delete meeting from collection, add document to history_meetings



                            //create historyMeeting document and put into history_meetings collection
                            DocumentReference documentReference = fStore.collection("history_meetings").document();
                            Map<String, Object> historyMeeting = new HashMap<>();
                            historyMeeting.put("course", curr_course);
                            historyMeeting.put("student_id", curr_student_id);
                            historyMeeting.put("time_period", curr_time_period);
                            historyMeeting.put("tutor_id", curr_tutor_id);

                            documentReference.set(historyMeeting).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "History Meeting Created", Toast.LENGTH_SHORT).show();
                                }
                            });

                            fStore.collection("meetings").document(curr_meeting_id).delete();
                            Intent intent = new Intent(context, TutorHistoryActivity.class);
                            intent.putExtra(StudentRegisterActivity.GET_USER_KEY, tutor);
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
                tvname = v.findViewById(R.id.tutor_ongoing_name);
                tvccode = v.findViewById(R.id.tutor_ongoing_ccode);
                tvtime = v.findViewById(R.id.tutor_ongoing_time);
                tvpnumber = v.findViewById(R.id.tutor_ongoing_pnumber);
            }
        }
}
