package com.cs180project.ucrclasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
    private ArrayList<Map<String, String>> mDataSource;

    public ClassListAdapter(Context context, ArrayList<Map<String, String>> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        TextView subtitleTextView = (TextView) rowView.findViewById(R.id.class_list_subtitle);
        TextView detailTextView = (TextView) rowView.findViewById(R.id.class_list_detail);

        Map<String, String> myclass = (Map<String, String>) getItem(position);
        titleTextView.setText(myclass.get("CourseNum"));
        subtitleTextView.setText(myclass.get("CourseTitle"));
        //detailTextView.setText(myclass.get("CatalogDescription"));

        return rowView;
    }

    public void clear() {
        mDataSource.clear();
    }

    public void sort() {
        Collections.sort(mDataSource, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                return o1.get("CourseNum").compareTo(o2.get("CourseNum"));
            }
        });
    }

    public void add(Map<String, String> newclass) {
        mDataSource.add(newclass);
    }
}
