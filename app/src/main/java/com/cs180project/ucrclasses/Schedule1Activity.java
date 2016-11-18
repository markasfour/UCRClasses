package com.cs180project.ucrclasses;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Schedule1Activity extends Fragment{
    View myView;
    CheckBox checkcheckcheck;

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
        final ViewGroup fcontainer = container;
        myView = inflater.inflate(R.layout.schedule1activity, container, false);
        getActivity().setTitle("Search Classes");

        //initialize checkbox
        checkcheckcheck = (CheckBox) myView.findViewById(R.id.checkcheckheck);

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
        qdropdown.setAdapter(qadapter); //qadapter.add("ALL"); All is not an option for quarters
        sdropdown.setAdapter(sadapter); sadapter.add("ALL");
        cdropdown.setAdapter(cadapter); cadapter.add("ALL");
        idropdown.setAdapter(iadapter); iadapter.add("ALL");
        tdropdown.setAdapter(tadapter); tadapter.add("ALL");

        SortedSet<String> subjects = new TreeSet<String>();
        //Initialize the quarter and subject dropdowns. This only needs to happen once since they never change
        for (Map.Entry<String, Map<String, Map<String, UCRCourse>>> quarters : Databaser.dat.entrySet()) { //Quarter Loop
            qadapter.add(quarters.getKey());
            for(Map.Entry<String, Map<String, UCRCourse>> classes : quarters.getValue().entrySet()) { //Subject Loop
                subjects.add(classes.getKey().trim());
            }
        }
        for(String str : subjects) sadapter.add(str); //Make sure the subjects are in alph order

        if(!SettingsActivity.term.equals("init")) {
            qdropdown.setSelection(qadapter.getPosition(SettingsActivity.term));
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
                UCRSchedules.displayCourse(((UCRCourse)ladapter.getItem(position)), 0, fcontainer.getContext(), 0);
            }

        });

        if(checkcheckcheck.isChecked()){
            final Dialog scheduleDialog = new Dialog(fcontainer.getContext());
            scheduleDialog.setContentView(R.layout.schedule_select);

            Button sched1Button = (Button) scheduleDialog.findViewById(R.id.schedule1Button);
            Button sched2Button = (Button) scheduleDialog.findViewById(R.id.schedule2Button);
            Button sched3Button = (Button) scheduleDialog.findViewById(R.id.schedule3Button);
            Button schedCancelButton = (Button) scheduleDialog.findViewById(R.id.scheduleCancelButton);

            sched1Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    dialog.dismiss();
                    scheduleDialog.dismiss();
                    Log.d("????POPUP?????", "clicked 1");
                }
            });

            sched2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    dialog.dismiss();
                    scheduleDialog.dismiss();
                    Log.d("????POPUP?????", "clicked 2");
                }
            });

            sched3Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    dialog.dismiss();
                    scheduleDialog.dismiss();
                    Log.d("????POPUP?????", "clicked 3");
                }
            });

            schedCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scheduleDialog.dismiss();
                    checkcheckcheck.toggle();
                    Log.d("????POPUP?????", "clicked cancel");
                }
            });

            scheduleDialog.show();
        }

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
        tadapter.clear();


        SortedSet<String> courses = new TreeSet<String>();
        SortedSet<String> profs = new TreeSet<String>();
        SortedSet<String> types = new TreeSet<String>();
        for (Map.Entry<String, Map<String, Map<String, UCRCourse>>> quarters : Databaser.dat.entrySet()) { //Quarter Loop
            if(!quarter.equals("ALL") && !quarter.equals(quarters.getKey())) continue; //Check our quarter choice
            for(Map.Entry<String, Map<String, UCRCourse>> classes : quarters.getValue().entrySet()) { //Subject Loop
                if(!subject.equals("ALL") && !subject.equals(classes.getKey().trim())) continue; //Check subject choice
                for (Map.Entry<String, UCRCourse> callNums : classes.getValue().entrySet()) { //Call nums loop (courses)
                    String course = callNums.getValue().courseNum;
                    if(course.indexOf('-') == -1) {
                        Log.d("ERROR", "Course with call num " + callNums.getKey() + " has no dash in CourseNum: " + course);
                        continue;
                    }
                    course = course.substring(0, course.indexOf('-')).trim();
                    courses.add(course);
                    if(!courseNum.equals("ALL") && !courseNum.equals(course)) continue; //Check course number choice
                    profs.add(callNums.getValue().instructor);
                    if(!instr.equals("ALL") && !instr.equals(callNums.getValue().instructor)) continue; //Check instructor choice
                    if(!callNums.getValue().courseType.trim().equals("")) types.add(callNums.getValue().courseType);
                    if(!type.equals("ALL") && !type.equals(callNums.getValue().courseType)) continue; //Check course type choice
                    ladapter.add(callNums.getValue());
                }
            }
        }

        ladapter.sort();
        cadapter.add("ALL"); iadapter.add("ALL"); tadapter.add("ALL");
        for(String str : courses) cadapter.add(str);
        for(String str : profs) iadapter.add(str);
        for(String str : types) tadapter.add(str);
        mListView.invalidateViews(); //Force table to refresh

        Log.d("!!!!!!MAJOR!!!!!!", SettingsActivity.major);
        Log.d("!!!!!!LEVEL!!!!!!", SettingsActivity.level);
        Log.d("!!!!!!TERM!!!!!!", SettingsActivity.term);
    }
}