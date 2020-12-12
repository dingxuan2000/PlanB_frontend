package com.example.controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class StudentHistoryCustomListView extends ArrayAdapter<String> {

    private ArrayList<String> name;
    private ArrayList<String> meeting_time;
    private ArrayList<String> course_id;
    private Activity context;

    public StudentHistoryCustomListView(Activity context, ArrayList<String> name, ArrayList<String> meeting_time, ArrayList<String> course_id){
        super(context, R.layout.student_history_listview_layout, name);

        this.context=context;
        this.name=name;
        this.meeting_time=meeting_time;
        this.course_id=course_id;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.student_history_listview_layout, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder.tvw1.setText(name.get(position));
        viewHolder.tvw2.setText(course_id.get(position));
        viewHolder.tvw4.setText(meeting_time.get(position));

        return r;



    }

    class ViewHolder
    {
        TextView tvw1;
        TextView tvw2;
        TextView tvw4;
        ViewHolder(View v){
            tvw1 = (TextView)v.findViewById(R.id.name);
            tvw2 = (TextView)v.findViewById(R.id.meeting_time);
            tvw4 = (TextView)v.findViewById(R.id.course_id);
        }
    }
}
