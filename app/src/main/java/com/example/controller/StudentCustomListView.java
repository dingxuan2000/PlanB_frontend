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

public class StudentCustomListView extends BaseAdapter implements Filterable
{
    Context c;
    ArrayList<tutorInfo> originalArray,tempArray;
    CustomFilter cs;
    String course_1,course_2,course_3,course_4;


    public StudentCustomListView(Context c, ArrayList<tutorInfo> originalArray)
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
        View row = inflater.inflate(R.layout.student_main_listview_layout, null);

        TextView t1 = (TextView) row.findViewById(R.id.tutor_ranking_name);
        TextView t2 = (TextView) row.findViewById(R.id.tutor_ranking_major);
        TextView t3 = (TextView) row.findViewById(R.id.tutor_ranking_grade);
        ImageView i1 = (ImageView) row.findViewById(R.id.tutor_img);

        t1.setText(originalArray.get(position).getName());
        t2.setText(originalArray.get(position).getMajor());
        t3.setText(originalArray.get(position).getGrade());
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
                ArrayList<tutorInfo> filters = new ArrayList<>();

                for(int i=0;i<tempArray.size();i++){
                    course_1 = tempArray.get(i).getCourse_1();
                    course_2 = tempArray.get(i).getCourse_2();
                    course_3 = tempArray.get(i).getCourse_3();
                    course_4 = tempArray.get(i).getCourse_4();
                    //course_5 = tempArray.get(i).getCourse_5();
//                    if(course_1 == null){course_1 = "";}
//                    if(course_2 == null){course_2 = "";}
//                    if(course_3 == null){course_3 = "";}
//                    if(course_4 == null){course_4 = "";}
                    //if(course_5 == null){course_5 = "";}
                    if(tempArray.get(i).getName().toUpperCase().contains(constraint)||
                            tempArray.get(i).getMajor().toUpperCase().contains(constraint)||
                            course_1.toUpperCase().contains(constraint)||
                            course_2.toUpperCase().contains(constraint)||
                            course_3.toUpperCase().contains(constraint)||
                            course_4.toUpperCase().contains(constraint)){


                        tutorInfo sr = new tutorInfo(tempArray.get(i).getName(),
                                tempArray.get(i).getMajor(),
                                tempArray.get(i).getGrade(),
                                course_1, course_2, course_3, course_4);
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
            originalArray = (ArrayList<tutorInfo>)results.values;
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


//public class studentCustomListView extends ArrayAdapter<String> {
//
//    private Integer[] tutor_imgid;
//    private ArrayList<String> tutor_name;
//    private ArrayList<String> tutor_major;
//    private ArrayList<String> tutor_grade;
//
//    private Activity context;
//
//    public studentCustomListView(Activity context, ArrayList<String> tutor_name, ArrayList<String> tutor_major,
//                                 ArrayList<String> tutor_grade) {
//        super(context, R.layout.student_main_listview_layout, tutor_name);
//
//        this.context = context;
//        this.tutor_name = tutor_name;
//        this.tutor_major = tutor_major;
//        this.tutor_grade = tutor_grade;
//        //this.tutor_imgid = tutor_imgid;
//
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
//
//            View r = convertView;
//            ViewHolder viewHolder;
//            if(r == null) {
//                LayoutInflater layoutInflater=context.getLayoutInflater();
//                r = layoutInflater.inflate(R.layout.student_main_listview_layout,null,true);
//                viewHolder = new ViewHolder(r);
//                r.setTag(viewHolder);
//            }
//            else {
//                viewHolder = (ViewHolder) r.getTag();
//            }
//
//            //viewHolder.ivt.setImageResource(tutor_imgid.get(position0);
//            viewHolder.tvname.setText(tutor_name.get(position));
//            viewHolder.tvmajor.setText(tutor_major.get(position));
//            viewHolder.tvgrade.setText(tutor_grade.get(position));
//
//
//            return r;
//        }
//
//        class ViewHolder{
//            TextView tvname;
//            TextView tvmajor;
//            TextView tvgrade;
//            //ImageView ivt;
//            ViewHolder(View v){
//                tvname = v.findViewById(R.id.tutor_ranking_name);
//                tvmajor = v.findViewById(R.id.tutor_ranking_major);
//                tvgrade = v.findViewById(R.id.tutor_ranking_grade);
//                //ivt = v.findViewById(R.id.tutor_img);
//            }
//        }
//}
