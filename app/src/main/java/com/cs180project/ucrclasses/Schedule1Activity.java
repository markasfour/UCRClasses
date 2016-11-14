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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
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

    final Map<String, Map<String, Map<String, UCRCourse>>> dat = new HashMap<String, Map<String, Map<String, UCRCourse>>>();

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

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

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
        qdropdown.setAdapter(qadapter);
        sdropdown.setAdapter(sadapter);
        cdropdown.setAdapter(cadapter);
        idropdown.setAdapter(iadapter);

        tdropdown.setAdapter(tadapter);
        tadapter.add("ALL"); //Populate the dropdown with all possible options
        tadapter.add("LEC"); tadapter.add("LAB"); tadapter.add("DIS"); tadapter.add("SEM");
        tadapter.add("WRK"); tadapter.add("STU"); tadapter.add("THE"); tadapter.add("SCR");
        tadapter.add("INT"); tadapter.add("IND"); tadapter.add("RES"); tadapter.add("COL");
        tadapter.add("PRC"); tadapter.add("FLD"); tadapter.add("CON"); tadapter.add("TUT");
        tadapter.add("CLN"); tadapter.add("ACT"); tadapter.add("LCA"); tadapter.add("WWK");


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

        //fetch all the data and store it in a map for the duration of the program
        //on actual devices this takes a bit of time unless the wifi is perfect, so
        //TODO: run this during the app startup splash screen
        //TODO: detach this hook after run so if the database is updated we don't care
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dat.clear(); qadapter.clear(); sadapter.clear(); cadapter.clear(); iadapter.clear();
                qadapter.add("ALL"); sadapter.add("ALL"); cadapter.add("ALL"); iadapter.add("ALL");
                SortedSet<String> profs = new TreeSet<String>();
                SortedSet<String> courses = new TreeSet<String>();

                for (DataSnapshot quarters: dataSnapshot.getChildren()) {
                    Map<String, Map<String, UCRCourse>> mclasses = new HashMap<String, Map<String, UCRCourse>>();
                    qadapter.add(quarters.getKey());
                    for(DataSnapshot classes: quarters.getChildren()) {


                        Map<String, UCRCourse> mcourse_nums = new HashMap<String, UCRCourse>();
                        sadapter.add(classes.getKey());
                        for(DataSnapshot callNums: classes.getChildren()) {

                            if(callNums.hasChildren()) {
                                String course = (String) callNums.child("CourseNum").getValue();
                                //Log.d("Breaking: ", "Quarter: " + quarters.getKey() + "\tSubject: " + classes.getKey() + "\tCall Num: " + callNums.getKey() + "\tCourse: " + course);
                                courses.add(course.substring(0, 7));
                                profs.add((String) callNums.child("Instructor").getValue());

                                //Yank everything from the db and never have to deal with it again
                                mcourse_nums.put(callNums.getKey(), new UCRCourse(
                                        callNums.child("AvailableSeats").getValue().toString(),
                                        callNums.child("MaxEnrollment").getValue().toString(),
                                        callNums.child("NumberonWaitList").getValue().toString(),
                                        callNums.child("WaitListMax").getValue().toString(),
                                        callNums.child("BuildingName").getValue().toString(),
                                        callNums.child("CallNo").getValue().toString(),
                                        callNums.child("CatalogDescription").getValue().toString(),
                                        callNums.child("Co-requisites").getValue().toString(),
                                        callNums.child("CourseNum").getValue().toString(),
                                        callNums.child("CourseTitle").getValue().toString(),
                                        callNums.child("Days").getValue().toString(),
                                        callNums.child("FinalExamDate").getValue().toString(),
                                        callNums.child("FinalExamTime").getValue().toString(),
                                        callNums.child("Instructor").getValue().toString(),
                                        callNums.child("Lec_Dis").getValue().toString(),
                                        callNums.child("Prerequisites").getValue().toString(),
                                        callNums.child("Restrictions").getValue().toString(),
                                        callNums.child("RoomAbrv").getValue().toString(),
                                        callNums.child("Subject").getValue().toString(),
                                        callNums.child("Time").getValue().toString(),
                                        callNums.child("Units").getValue().toString()));
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
        for (Map.Entry<String, Map<String, Map<String, UCRCourse>>> quarters : dat.entrySet()) { //Quarter Loop
            if(!quarter.equals("ALL") && !quarter.equals(quarters.getKey())) continue; //Check our quarter choice
            for(Map.Entry<String, Map<String, UCRCourse>> classes : quarters.getValue().entrySet()) { //Subject Loop
                if(!subject.equals("ALL") && !subject.equals(classes.getKey())) continue; //Check subject choice
                for (Map.Entry<String, UCRCourse> callNums : classes.getValue().entrySet()) { //Call nums loop (courses)
                    String course = callNums.getValue().courseNum;
                    course = course.substring(0, 7);
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