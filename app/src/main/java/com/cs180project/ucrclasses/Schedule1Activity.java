package com.cs180project.ucrclasses;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

        SortedSet<String> subjects = new TreeSet<String>();
        //Initialize the quarter and subject dropdowns. This only needs to happen once since they never change
        for (Map.Entry<String, Map<String, Map<String, UCRCourse>>> quarters : Databaser.dat.entrySet()) { //Quarter Loop
            qadapter.add(quarters.getKey());
            for(Map.Entry<String, Map<String, UCRCourse>> classes : quarters.getValue().entrySet()) { //Subject Loop
                subjects.add(classes.getKey());
            }
        }
        for(String str : subjects) sadapter.add(str); //Make sure the subjects are in alph order


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
                // custom dialog
                final Dialog dialog = new Dialog(fcontainer.getContext());
                dialog.setContentView(R.layout.class_popup);
                dialog.setTitle(((UCRCourse)ladapter.getItem(position)).courseNum);

                // set the custom dialog components - text, image and button
                //TextView titleText = (TextView) dialog.findViewById(R.id.popup_title);
                //titleText.setText();
                TextView callnoText = (TextView) dialog.findViewById(R.id.popup_callno);
                callnoText.setText(((UCRCourse)ladapter.getItem(position)).callNum);

                TextView typeText = (TextView) dialog.findViewById(R.id.popup_classtype);
                typeText.setText(((UCRCourse)ladapter.getItem(position)).courseType);


                TextView timesText = (TextView) dialog.findViewById(R.id.popup_times);
                timesText.setText(((UCRCourse)ladapter.getItem(position)).time);

                TextView instrText = (TextView) dialog.findViewById(R.id.popup_instr);
                instrText.setText(((UCRCourse)ladapter.getItem(position)).instructor);

                TextView seatsText = (TextView) dialog.findViewById(R.id.popup_seats);
                seatsText.setText(((UCRCourse)ladapter.getItem(position)).availableSeats + "/" + ((UCRCourse)ladapter.getItem(position)).maxEnrollment);

                TextView waitlistText = (TextView) dialog.findViewById(R.id.popup_waitlist);
                waitlistText.setText(((UCRCourse)ladapter.getItem(position)).numOnWaitlist + "/" + ((UCRCourse)ladapter.getItem(position)).maxWaitlist);

                TextView descText = (TextView) dialog.findViewById(R.id.popup_description);
                descText.setText(((UCRCourse)ladapter.getItem(position)).catalogDescription);
                //ImageView image = (ImageView) dialog.findViewById(R.id.image);
                //image.setImageResource(R.drawable.smaller_ucr_seal_white);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                if(dialogButton == null) Log.d("Uh oh", "So dialog button was null??");
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
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