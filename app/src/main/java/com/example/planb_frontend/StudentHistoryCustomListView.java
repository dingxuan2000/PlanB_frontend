package com.example.planb_frontend;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StudentHistoryCustomListView extends ArrayAdapter<String> {

    private String[] names;
    private String[] time;
    private String[] course;
    private Integer[] tutor_imgid;
    private Activity context;

    public StudentHistoryCustomListView(Activity context, String[] names, String[] time, String[] course, Integer[] tutor_imgid){
        super(context, R.layout.student_history_listview_layout, names);

        this.context=context;
        this.names=names;
        this.time=time;
        this.course=course;
        this.tutor_imgid=tutor_imgid;

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

        viewHolder.tvw1.setText(names[position]);
        viewHolder.tvw2.setText(time[position]);
        viewHolder.tvw4.setText(course[position]);
        viewHolder.tvw5.setImageResource(tutor_imgid[position]);

        return r;



    }

    class ViewHolder
    {
        TextView tvw1;
        TextView tvw2;
        TextView tvw4;
        ImageView tvw5;
        ViewHolder(View v){
            tvw1 = (TextView)v.findViewById(R.id.names);
            tvw2 = (TextView)v.findViewById(R.id.time);
            tvw4 = (TextView)v.findViewById(R.id.course);
            tvw5 = (ImageView)v.findViewById(R.id.tutor_img);
        }
    }
}
