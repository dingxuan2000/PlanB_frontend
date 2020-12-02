package com.example.planb_frontend;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TutorCCustomListView extends ArrayAdapter<String> {

    //private Integer[] tutor_imgid;
    private ArrayList<String> student_name;
    private ArrayList<String> course_code;
    private ArrayList<String> time;
    private ArrayList<String> student_phone_number;

    private Activity context;

    public TutorCCustomListView(Activity context, ArrayList<String> student_name, ArrayList<String> course_code,
                                ArrayList<String> time, ArrayList<String> student_phone_number) {
        super(context, R.layout.tutor_connection_listview_layout, student_name);

        this.context = context;
        this.student_name = student_name;
        this.course_code = course_code;
        this.time = time;
        this.student_phone_number = student_phone_number;
        //this.tutor_imgid = tutor_imgid;

        }

        @NonNull
        @Override
        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {

            View r = convertView;
            ViewHolder viewHolder;
            if(r == null) {
                LayoutInflater layoutInflater=context.getLayoutInflater();
                r = layoutInflater.inflate(R.layout.tutor_connection_listview_layout,null,true);
                viewHolder = new ViewHolder(r);
                r.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) r.getTag();
            }

            //viewHolder.ivt.setImageResource(tutor_imgid.get(position0);
            viewHolder.tvname.setText(student_name.get(position));
            viewHolder.tvccode.setText(course_code.get(position));
            viewHolder.tvtime.setText(time.get(position));
            viewHolder.tvpnumber.setText(student_phone_number.get(position));


            //Handle buttons and add onClickListeners
            Button startBtn = r.findViewById(R.id.start_Btn);
            Button cancelBtn = r.findViewById(R.id.cancel_Btn);

            startBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something

                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something

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
                //ivt = v.findViewById(R.id.tutor_img);
            }
        }
}