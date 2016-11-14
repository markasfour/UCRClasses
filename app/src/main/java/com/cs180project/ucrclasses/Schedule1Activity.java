package com.cs180project.ucrclasses;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Schedule1Activity extends Fragment{
    View myView;

    //Setup dropdowns and their adapters
    ListView mListView;
    ClassListAdapter ladapter;

    Spinner qdropdown;
    ArrayAdapter<String> qadapter;

    Spinner sdropdown;
    ArrayAdapter<String> sadapter;

    Spinner cdropdown;
    ArrayAdapter<String> cadapter;

    Spinner idropdown;
    ArrayAdapter<String> iadapter;

    Spinner tdropdown;
    ArrayAdapter<String> tadapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.schedule1activity, container, false);

        //Setup dropdowns and their adapters
        mListView = (ListView) myView.findViewById(R.id.class_list);
        ladapter = new ClassListAdapter(getActivity().getApplicationContext(), new ArrayList<UCRCourse>(), mListView);

        qdropdown = (Spinner)myView.findViewById(R.id.quarter_spinner);
        qadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);

        sdropdown = (Spinner)myView.findViewById(R.id.subject_spinner);
        sadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);

        cdropdown = (Spinner)myView.findViewById(R.id.course_spinner);
        cadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);

        idropdown = (Spinner)myView.findViewById(R.id.instr_spinner);
        iadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);

        tdropdown = (Spinner)myView.findViewById(R.id.classtype_spinner);
        tadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item);

        mListView.setAdapter(ladapter);
        qdropdown.setAdapter(qadapter); qadapter.add("ALL");
        sdropdown.setAdapter(sadapter); sadapter.add("ALL");
        cdropdown.setAdapter(cadapter); cadapter.add("ALL");
        idropdown.setAdapter(iadapter); iadapter.add("ALL");

        tdropdown.setAdapter(tadapter);
        tadapter.add("ALL"); //Populate the dropdown with all possible options
        tadapter.add("LEC"); tadapter.add("LAB"); tadapter.add("DIS"); tadapter.add("SEM");
        tadapter.add("WRK"); tadapter.add("STU"); tadapter.add("THE"); tadapter.add("SCR");
        tadapter.add("INT"); tadapter.add("IND"); tadapter.add("RES"); tadapter.add("COL");
        tadapter.add("PRC"); tadapter.add("FLD"); tadapter.add("CON"); tadapter.add("TUT");
        tadapter.add("CLN"); tadapter.add("ACT"); tadapter.add("LCA"); tadapter.add("WWK");

        //Initialize the quarter and subject dropdowns. This only needs to happen once since they never change
        for (Map.Entry<String, Map<String, Map<String, UCRCourse>>> quarters : Databaser.dat.entrySet()) { //Quarter Loop
            qadapter.add(quarters.getKey());
            for(Map.Entry<String, Map<String, UCRCourse>> classes : quarters.getValue().entrySet()) { //Subject Loop
                sadapter.add(classes.getKey());
            }
        }


        //When they select a quarter...
        qdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                sdropdown.setSelection(0); //Set dropdowns below us to "ALL"
                cdropdown.setSelection(0);
                idropdown.setSelection(0);
                tdropdown.setSelection(0);
                filterCourseList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        //When they select a subject...
        sdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                cdropdown.setSelection(0); //Set dropdowns below us to "ALL"
                idropdown.setSelection(0);
                tdropdown.setSelection(0);
                filterCourseList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        //When they select a course number...
        cdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                idropdown.setSelection(0); //Set dropdowns below us to "ALL"
                tdropdown.setSelection(0);
                filterCourseList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        //When they select an instructor...
        idropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                tdropdown.setSelection(0); //Set dropdowns below us to "ALL"
                filterCourseList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        //When they select a course type...
        tdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                filterCourseList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        //When they click a course in the table..
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast toast = Toast.makeText(getActivity().getApplicationContext(), ((Map<String, String>)ladapter.getItem(position)).get("CourseNum") + " Clicked!", Toast.LENGTH_SHORT);
                //toast.show();
            }

        });

        return myView;
    }

    private void filterCourseList() {
        if(qdropdown.getSelectedItem() == null || sdropdown.getSelectedItem() == null ||
                cdropdown.getSelectedItem() == null || idropdown.getSelectedItem() == null ||
                tdropdown.getSelectedItem() == null) return;

        String quarter = qdropdown.getSelectedItem().toString();
        String subject = sdropdown.getSelectedItem().toString();
        String courseNum = cdropdown.getSelectedItem().toString();
        String instr = idropdown.getSelectedItem().toString();
        String type = tdropdown.getSelectedItem().toString();

        //Clear courses and instructors dropdowns
        cadapter.clear();
        iadapter.clear();
        ladapter.clear();


        SortedSet<String> courses = new TreeSet<String>();
        SortedSet<String> profs = new TreeSet<String>();
        for (Map.Entry<String, Map<String, Map<String, UCRCourse>>> quarters : Databaser.dat.entrySet()) { //Quarter Loop
            if(!quarter.equals("ALL") && !quarter.equals(quarters.getKey())) continue; //Check our quarter choice
            for(Map.Entry<String, Map<String, UCRCourse>> classes : quarters.getValue().entrySet()) { //Subject Loop
                if(!subject.equals("ALL") && !subject.equals(classes.getKey())) continue; //Check subject choice
                for (Map.Entry<String, UCRCourse> callNums : classes.getValue().entrySet()) { //Call nums loop (courses)
                    String course = callNums.getValue().courseNum;
                    if(course.indexOf('-') == -1) {
                        Log.d("ERROR", "Course with call num " + callNums.getKey() + " has no dash in CourseNum: " + course);
                        continue;
                    }
                    courses.add(course.substring(0, course.indexOf('-')).trim());
                    courses.add(course);
                    if(!courseNum.equals("ALL") && !courseNum.equals(course)) continue; //Check course number choice
                    profs.add(callNums.getValue().instructor);
                    if(!instr.equals("ALL") && !instr.equals(callNums.getValue().instructor)) continue; //Check instructor choice
                    if(!type.equals("ALL") && !type.equals(callNums.getValue().courseType)) continue; //Check course type choice
                    ladapter.add(callNums.getValue());
                }
            }
        }

        ladapter.sort();
        cadapter.add("ALL"); iadapter.add("ALL");
        for(String str : courses) cadapter.add(str);
        for(String str : profs) iadapter.add(str);
        mListView.invalidateViews(); //Force table to refresh
    }
}