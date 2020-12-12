package com.example.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//public class TutorCustomListView extends ArrayAdapter<String> {
//
//    //private String[] names;
//    private ArrayList<String> names;
//    private ArrayList<String> time;
//    private ArrayList<String> major;
//    private ArrayList<String> course;
//    //    private String[] time;
////    private String[] major;
////    private String[] course;
//    private Activity context;
//
//    public TutorCustomListView(Activity context, ArrayList<String> names, ArrayList<String> time, ArrayList<String> major, ArrayList<String> course){
//        super(context, R.layout.listview_layout, names);
//
//        this.context=context;
//        this.names=names;
//        this.time=time;
//        this.major=major;
//        this.course=course;
//
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        View r=convertView;
//        ViewHolder viewHolder=null;
//        if(r==null){
//            LayoutInflater layoutInflater=context.getLayoutInflater();
//            r=layoutInflater.inflate(R.layout.listview_layout, null, true);
//            viewHolder = new ViewHolder(r);
//            r.setTag(viewHolder);
//        }
//        else{
//            viewHolder = (ViewHolder) r.getTag();
//        }
//
//        //viewHolder.tvw1.setText(names[position]);
//        viewHolder.tvw1.setText(names.get(position));
//        viewHolder.tvw2.setText(time.get(position));
//        viewHolder.tvw3.setText(major.get(position));
//        viewHolder.tvw4.setText(course.get(position));
//
//        return r;
//
//
//
//    }
//
//    class ViewHolder
//    {
//        TextView tvw1;
//        TextView tvw2;
//        TextView tvw3;
//        TextView tvw4;
//        ViewHolder(View v){
//            tvw1 = (TextView)v.findViewById(R.id.names);
//            tvw2 = (TextView)v.findViewById(R.id.time);
//            tvw3 = (TextView)v.findViewById(R.id.major);
//            tvw4 = (TextView)v.findViewById(R.id.course);
//        }
//    }
//}

public class TutorCustomListView extends BaseAdapter implements Filterable
{
    Context c;
    ArrayList<ticketInfo> originalArray,tempArray;
    CustomFilter cs;


    public TutorCustomListView(Context c, ArrayList<ticketInfo> originalArray)
    {
        this.c = c;
        this.originalArray = originalArray;
        this.tempArray = originalArray;
    }



    @Override
    public Object getItem(int position) {
        return originalArray.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_layout, null);

        TextView t1 = (TextView) row.findViewById(R.id.names);
        TextView t2 = (TextView) row.findViewById(R.id.major);
        TextView t3 = (TextView) row.findViewById(R.id.course);
        TextView t4 = (TextView) row.findViewById(R.id.time);
        ImageView i1 = (ImageView) row.findViewById(R.id.tutor_img);

        t1.setText(originalArray.get(position).getName());
        t2.setText(originalArray.get(position).getMajor());
        t3.setText(originalArray.get(position).getCourse());
        t4.setText(originalArray.get(position).getTime());

        //i1.setImageResource(originalArray.get(position).getImage());

        return row;
    }

    @Override
    public Filter getFilter()
    {
        if(cs == null)
        {
            cs = new CustomFilter();
        }
        return cs;
    }

    class CustomFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();

            if(constraint != null & constraint.length()>0){
                constraint = constraint.toString().toUpperCase();
                ArrayList<ticketInfo> filters = new ArrayList<>();

                for(int i=0;i<tempArray.size();i++){
                    if(tempArray.get(i).getName().toUpperCase().contains(constraint)||
                            tempArray.get(i).getCourse().toUpperCase().contains(constraint)){


                        ticketInfo sr = new ticketInfo(tempArray.get(i).getName(),
                                tempArray.get(i).getMajor(),
                                tempArray.get(i).getCourse(),
                                tempArray.get(i).getTime());
                        //tempArray.get(i).getImage());
                        filters.add(sr);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            }
            else{
                results.count = tempArray.size();
                results.values = tempArray;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            originalArray = (ArrayList<ticketInfo>)results.values;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return originalArray.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
