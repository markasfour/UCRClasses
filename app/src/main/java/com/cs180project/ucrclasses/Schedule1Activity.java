package com.cs180project.ucrclasses;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Schedule1Activity extends Fragment{
    View myView;

    final Map<String, Map<String, Map<String, Map<String, String>>>> dat = new HashMap<String, Map<String, Map<String, Map<String, String>>>>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.schedule1activity, container, false);

//        Toolbar toolbar = (Toolbar) myView.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        Spinner qdropdown = (Spinner)myView.findViewById(R.id.quarter_spinner);
        final ArrayAdapter<String> qadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        qdropdown.setAdapter(qadapter);

        final Spinner sdropdown = (Spinner)myView.findViewById(R.id.subject_spinner);
        final ArrayAdapter<String> sadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        sdropdown.setAdapter(sadapter);

        final Spinner cdropdown = (Spinner)myView.findViewById(R.id.course_spinner);
        final ArrayAdapter<String> cadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        cdropdown.setAdapter(cadapter);

        Spinner idropdown = (Spinner)myView.findViewById(R.id.instr_spinner);
        final ArrayAdapter<String> iadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        idropdown.setAdapter(iadapter);



        sdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final String  mselection = sdropdown.getSelectedItem().toString();
                Toast.makeText(getActivity().getApplicationContext(), "selected "+ mselection, Toast.LENGTH_SHORT).show();

                Log.d("TEST", mselection + " was selected!");
                //Clear courses and instructors dropdowns
                cadapter.clear();
                iadapter.clear();
                SortedSet<String> courses = new TreeSet<String>();
                SortedSet<String> profs = new TreeSet<String>();
                for (Map.Entry<String, Map<String, Map<String, Map<String, String>>>> quarters : dat.entrySet()) {
                    for(Map.Entry<String, Map<String, Map<String, String>>> classes : quarters.getValue().entrySet()) {
                        if(mselection.equals("ALL") || mselection.equals((String) classes.getKey())) {
                            for (Map.Entry<String, Map<String, String>> callNums : classes.getValue().entrySet()) {
                                String course = callNums.getValue().get("CourseNum");// child("CourseNum").getValue();
                                course = course.substring(0, 7);
                                courses.add(course);
                                profs.add(callNums.getValue().get("Instructor"));
                            }
                        }
                    }
                }

                cadapter.add("ALL"); iadapter.add("ALL");
                for(String str : courses) cadapter.add(str);
                for(String str : profs) iadapter.add(str);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        cdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final String  mselection = cdropdown.getSelectedItem().toString();
                Toast.makeText(getActivity().getApplicationContext(), "selected "+ mselection, Toast.LENGTH_SHORT).show();

                Log.d("TEST", mselection + " was selected!");
                iadapter.clear();
                SortedSet<String> profs = new TreeSet<String>();
                for (Map.Entry<String, Map<String, Map<String, Map<String, String>>>> quarters : dat.entrySet()) {
                    for(Map.Entry<String, Map<String, Map<String, String>>> classes : quarters.getValue().entrySet()) {
                        if(sdropdown.getSelectedItem().equals("ALL") || sdropdown.getSelectedItem().equals((String) classes.getKey())) {
                            for (Map.Entry<String, Map<String, String>> callNums : classes.getValue().entrySet()) {
                                String course = callNums.getValue().get("CourseNum");// child("CourseNum").getValue();
                                course = course.substring(0, 7);
                                if (mselection.equals("ALL") || course.equals(mselection)) {
                                    profs.add(callNums.getValue().get("Instructor"));
                                }
                            }
                        }
                    }
                }

                iadapter.add("ALL");
                for(String str : profs) iadapter.add(str);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        //fetch all the data and store it in a map for the duration of the program
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dat.clear(); qadapter.clear(); sadapter.clear(); cadapter.clear(); iadapter.clear();
                qadapter.add("ALL"); sadapter.add("ALL"); cadapter.add("ALL"); iadapter.add("ALL");
                SortedSet<String> profs = new TreeSet<String>();
                SortedSet<String> courses = new TreeSet<String>();


                for (DataSnapshot quarters: dataSnapshot.getChildren()) {
                    Map<String, Map<String, Map<String, String>>> mclasses = new HashMap<String, Map<String, Map<String, String>>>();
                    qadapter.add(quarters.getKey());
                    for(DataSnapshot classes: quarters.getChildren()) {


                        Map<String, Map<String, String>> mcourse_nums = new HashMap<String, Map<String, String>>();
                        sadapter.add(classes.getKey());
                        for(DataSnapshot callNums: classes.getChildren()) {


                            Map<String, String> minfo = new HashMap<String, String>();

                            if(callNums.hasChildren()) {
                                String course = (String) callNums.child("CourseNum").getValue();
                                courses.add(course.substring(0, 7));
                                profs.add((String) callNums.child("Instructor").getValue());

                                for(DataSnapshot field : callNums.getChildren()) {
                                    minfo.put(field.getKey(), (String) field.getValue());
                                }
                                mcourse_nums.put(callNums.getKey(), minfo);
                            }

                        }
                        mclasses.put(classes.getKey(), mcourse_nums);
                    }
                    dat.put(quarters.getKey(), mclasses);
                }

                for(String str : courses) cadapter.add(str);
                for(String str : profs) iadapter.add(str);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) { }
        });

        return myView;
    }
}