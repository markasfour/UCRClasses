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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import static com.cs180project.ucrclasses.Databaser.dat;

public class Schedule1Activity extends Fragment{
    View myView;
    Button search_toggle;
    TableRow tr1;
    TableRow tr2;
    TableRow tr3;
    TableRow tr4;
    TableRow tr5;
    TableRow tr6;
    TableRow tr7;
    TableRow tr8;
    TableRow tr9;
    TableRow tr10;
    TableRow tr11;
    TableRow tr12;
    TableRow tr13;
    TableRow tr14;


    CheckBox checkcheckcheck;
    Button refresh;

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

    private int schedule = -1;
    private String defaultQuarter = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup fcontainer = container;
        myView = inflater.inflate(R.layout.schedule1activity, container, false);
        getActivity().setTitle("Search Classes");

        //search query toggle
        search_toggle = (Button) myView.findViewById(R.id.search);
        //search query table rows
        tr1 = (TableRow) myView.findViewById(R.id.tr1);
        tr2 = (TableRow) myView.findViewById(R.id.tr2);
        tr3 = (TableRow) myView.findViewById(R.id.tr3);
        tr4 = (TableRow) myView.findViewById(R.id.tr4);
        tr5 = (TableRow) myView.findViewById(R.id.tr5);
        tr6 = (TableRow) myView.findViewById(R.id.tr6);
        tr7 = (TableRow) myView.findViewById(R.id.tr7);
        tr8 = (TableRow) myView.findViewById(R.id.tr8);
        tr9 = (TableRow) myView.findViewById(R.id.tr9);
        tr10 = (TableRow) myView.findViewById(R.id.tr10);
        tr11 = (TableRow) myView.findViewById(R.id.tr11);
        tr12 = (TableRow) myView.findViewById(R.id.tr12);
        tr13 = (TableRow) myView.findViewById(R.id.tr13);
        tr14 = (TableRow) myView.findViewById(R.id.tr14);

        //initialize checkbox
        checkcheckcheck = (CheckBox) myView.findViewById(R.id.checkcheckheck);
        refresh = (Button) myView.findViewById(R.id.refresh);

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
        for (Map.Entry<String, Map<String, Map<String, UCRCourse>>> quarters : dat.entrySet()) { //Quarter Loop
            qadapter.add(quarters.getKey());
            for(Map.Entry<String, Map<String, UCRCourse>> classes : quarters.getValue().entrySet()) { //Subject Loop
                subjects.add(classes.getKey().trim());
            }
        }
        for(String str : subjects) sadapter.add(str); //Make sure the subjects are in alph order
        filterCourseList();

        if(!SettingsActivity.term.equals("init")) {
            if(SettingsActivity.term.equals("Summer")) {
                defaultQuarter = "17U";
            } else if(SettingsActivity.term.equals("Fall")) {
                defaultQuarter = "16F";
            } else if(SettingsActivity.term.equals("Winter")) {
                defaultQuarter = "17W";
            } else if(SettingsActivity.term.equals("Spring")) {
                defaultQuarter = "17S";
            }
            qdropdown.setSelection(qadapter.getPosition(defaultQuarter));
        }


        //TOGGLE SEARCH QUERY VISIBILITY
        search_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tr1.getVisibility() == View.VISIBLE){
                    tr1.setVisibility(View.GONE);
                    tr2.setVisibility(View.GONE);
                    tr3.setVisibility(View.GONE);
                    tr4.setVisibility(View.GONE);
                    tr5.setVisibility(View.GONE);
                    tr6.setVisibility(View.GONE);
                    tr7.setVisibility(View.GONE);
                    tr8.setVisibility(View.GONE);
                    tr9.setVisibility(View.GONE);
                    tr10.setVisibility(View.GONE);
                    tr11.setVisibility(View.GONE);
                    tr12.setVisibility(View.GONE);
                    tr13.setVisibility(View.GONE);
                    tr14.setVisibility(View.GONE);
                    search_toggle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, android.R.drawable.arrow_down_float, 0);
                }
                else {
                    tr1.setVisibility(View.VISIBLE);
                    tr2.setVisibility(View.VISIBLE);
                    tr3.setVisibility(View.VISIBLE);
                    tr4.setVisibility(View.VISIBLE);
                    tr5.setVisibility(View.VISIBLE);
                    tr6.setVisibility(View.VISIBLE);
                    tr7.setVisibility(View.VISIBLE);
                    tr8.setVisibility(View.VISIBLE);
                    tr9.setVisibility(View.VISIBLE);
                    tr10.setVisibility(View.VISIBLE);
                    tr11.setVisibility(View.VISIBLE);
                    tr12.setVisibility(View.VISIBLE);
                    tr13.setVisibility(View.VISIBLE);
                    tr14.setVisibility(View.VISIBLE);
                    search_toggle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, android.R.drawable.arrow_up_float, 0);
                }
            }
        });

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

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dat.isEmpty())   Databaser.fetchData();
            }
        });

        checkcheckcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkcheckcheck.isChecked()){
                    final Dialog scheduleDialog = new Dialog(fcontainer.getContext());
                    scheduleDialog.setContentView(R.layout.schedule_select);

                    TextView sched1Title = (TextView) scheduleDialog.findViewById(R.id.sched_select_title);
                    sched1Title.setText("Which schedule?");

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
                            schedule = 0;
                            filterCourseList();
                        }
                    });

                    sched2Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                    dialog.dismiss();
                            scheduleDialog.dismiss();
                            Log.d("????POPUP?????", "clicked 2");
                            schedule = 1;
                            filterCourseList();
                        }
                    });

                    sched3Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                    dialog.dismiss();
                            scheduleDialog.dismiss();
                            Log.d("????POPUP?????", "clicked 3");
                            schedule = 2;
                            filterCourseList();
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
                else{
                    schedule = -1;
                    filterCourseList();
                }
            }
        });


        return myView;
    }

    private boolean isConflicting(UCRCourse course, int sschedule) {
        if(sschedule == -1 || course.days.equals("n/a")) return false;
        int cstart = (UCRSchedules.getStartHour(course) * 60) + UCRSchedules.getStartMin(course);
        int cend = (UCRSchedules.getEndHour(course) * 60) + UCRSchedules.getEndMin(course);

        for(int x = 0; x < UCRSchedules.getSize(sschedule); x++) {
            //Check if the days even overlap
            String sdays = UCRSchedules.getDays(sschedule, x);
            if(sdays.equals("n/a")) continue;
            boolean sameDay = false;
            for(int y = 0; y < sdays.length(); y++) {
                if(course.days.contains(Character.toString(sdays.charAt(y)))) {
                    sameDay = true; break;
                }
            }
            if(!sameDay) continue;

            //If days overlap then check if the times do
            int sstart = (UCRSchedules.getStartHour(sschedule, x) * 60) + UCRSchedules.getStartMin(sschedule, x);
            int send = (UCRSchedules.getEndHour(sschedule, x) * 60) + UCRSchedules.getEndMin(sschedule, x);
            if(cstart < send && sstart < cend) return true;
        }

        return false;
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
        for (Map.Entry<String, Map<String, Map<String, UCRCourse>>> quarters : dat.entrySet()) { //Quarter Loop
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
                    if(isConflicting(callNums.getValue(), schedule)) continue;
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
    }
}