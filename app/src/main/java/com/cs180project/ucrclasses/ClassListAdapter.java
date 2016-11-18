package com.cs180project.ucrclasses;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by aricohen on 11/12/16.
 */

public class ClassListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<UCRCourse> mDataSource;
    private ListView parentView;

    public ClassListAdapter(Context context, ArrayList<UCRCourse> items, ListView lv) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parentView = lv;
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_class, parent, false);

        TextView titleTextView = (TextView) rowView.findViewById(R.id.class_list_title);
        TextView timesTextView = (TextView) rowView.findViewById(R.id.class_list_times);
        TextView seatsTextView = (TextView) rowView.findViewById(R.id.class_list_seats);
        TextView lecdisTextView = (TextView) rowView.findViewById(R.id.class_list_lecdis);
        TextView profTextView = (TextView) rowView.findViewById(R.id.class_list_prof);

        UCRCourse myclass = (UCRCourse) getItem(position);
        titleTextView.setText(myclass.courseNum);
        if(myclass.days.equals("n/a") || myclass.time.equals("n/a"))
            timesTextView.setText("n/a");
        else
            timesTextView.setText(myclass.days  + ": " + myclass.time);
        seatsTextView.setText(myclass.availableSeats + "/" + myclass.maxEnrollment);
        lecdisTextView.setText(myclass.courseType);
        profTextView.setText(myclass.instructor);

        return rowView;
    }

    public void clear() {
        mDataSource.clear();
        parentView.invalidateViews();
    }

    public void sort() {
        Collections.sort(mDataSource, new Comparator<UCRCourse>() {
            @Override
            public int compare(UCRCourse o1, UCRCourse o2) {
                return o1.courseNum.compareTo(o2.courseNum);
            }
        });
        parentView.invalidateViews();
    }

    public void add(UCRCourse newclass) {
        mDataSource.add(newclass);
        parentView.invalidateViews();
    }
}
