package com.example.controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class StudentOngoingCustomListView extends ArrayAdapter<String> {
    
    private ArrayList<String> tutor_name;
    private ArrayList<String> course_code;
    private ArrayList<String> time;
    private ArrayList<String> phone_number;

    private Activity context;

    public StudentOngoingCustomListView(Activity context, ArrayList<String> tutor_name, ArrayList<String> course_code,
                                        ArrayList<String> time, ArrayList<String> phone_number) {
        super(context, R.layout.student_ongoing_listview_layout, tutor_name);

        this.context = context;
        this.tutor_name = tutor_name;
        this.course_code = course_code;
        this.time = time;
        this.phone_number = phone_number;

        }

        @NonNull
        @Override
        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {

            View r = convertView;
            ViewHolder viewHolder;
            if(r == null) {
                LayoutInflater layoutInflater=context.getLayoutInflater();
                r = layoutInflater.inflate(R.layout.student_ongoing_listview_layout,null,true);
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

            return r;
        }

        class ViewHolder{
            TextView tvname;
            TextView tvccode;
            TextView tvtime;
            TextView tvpnumber;


            ViewHolder(View v){
                tvname = v.findViewById(R.id.student_ongoing_name);
                tvccode = v.findViewById(R.id.student_ongoing_ccode);
                tvtime = v.findViewById(R.id.student_ongoing_time);
                tvpnumber = v.findViewById(R.id.student_ongoing_pnumber);
            }
        }
}
