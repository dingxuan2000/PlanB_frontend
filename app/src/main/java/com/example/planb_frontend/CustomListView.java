package com.example.planb_frontend;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomListView extends ArrayAdapter<String> {

    //private String[] names;
    private ArrayList<String> names;
    private ArrayList<String> time;
    private ArrayList<String> major;
    private ArrayList<String> course;
    //    private String[] time;
//    private String[] major;
//    private String[] course;
    private Activity context;

    public CustomListView(Activity context, ArrayList<String> names, ArrayList<String> time, ArrayList<String> major, ArrayList<String> course){
        super(context, R.layout.listview_layout, names);

        this.context=context;
        this.names=names;
        this.time=time;
        this.major=major;
        this.course=course;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.listview_layout, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) r.getTag();
        }

        //viewHolder.tvw1.setText(names[position]);
        viewHolder.tvw1.setText(names.get(position));
        viewHolder.tvw2.setText(time.get(position));
        viewHolder.tvw3.setText(major.get(position));
        viewHolder.tvw4.setText(course.get(position));

        return r;



    }

    class ViewHolder
    {
        TextView tvw1;
        TextView tvw2;
        TextView tvw3;
        TextView tvw4;
        ViewHolder(View v){
            tvw1 = (TextView)v.findViewById(R.id.names);
            tvw2 = (TextView)v.findViewById(R.id.time);
            tvw3 = (TextView)v.findViewById(R.id.major);
            tvw4 = (TextView)v.findViewById(R.id.course);
        }
    }
}
