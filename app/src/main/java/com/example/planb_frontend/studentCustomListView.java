package com.example.planb_frontend;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class studentCustomListView extends ArrayAdapter<String> {

    private Integer[] tutor_imgid;
    private ArrayList<String> tutor_name;
    private ArrayList<String> tutor_major;
    private ArrayList<String> tutor_grade;

    private Activity context;

    public studentCustomListView(Activity context, ArrayList<String> tutor_name, ArrayList<String> tutor_major,
                                 ArrayList<String> tutor_grade) {
        super(context, R.layout.student_main_listview_layout, tutor_name);

        this.context = context;
        this.tutor_name = tutor_name;
        this.tutor_major = tutor_major;
        this.tutor_grade = tutor_grade;
        //this.tutor_imgid = tutor_imgid;

        }

        @NonNull
        @Override
        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {

            View r = convertView;
            ViewHolder viewHolder;
            if(r == null) {
                LayoutInflater layoutInflater=context.getLayoutInflater();
                r = layoutInflater.inflate(R.layout.student_main_listview_layout,null,true);
                viewHolder = new ViewHolder(r);
                r.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) r.getTag();
            }

            //viewHolder.ivt.setImageResource(tutor_imgid.get(position0);
            viewHolder.tvname.setText(tutor_name.get(position));
            viewHolder.tvmajor.setText(tutor_major.get(position));
            viewHolder.tvgrade.setText(tutor_grade.get(position));


            return r;
        }

        class ViewHolder{
            TextView tvname;
            TextView tvmajor;
            TextView tvgrade;
            //ImageView ivt;
            ViewHolder(View v){
                tvname = v.findViewById(R.id.tutor_ranking_name);
                tvmajor = v.findViewById(R.id.tutor_ranking_major);
                tvgrade = v.findViewById(R.id.tutor_ranking_grade);
                //ivt = v.findViewById(R.id.tutor_img);
            }
        }
}
