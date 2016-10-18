package com.example.aricohen.test;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;


public class MainActivity extends AppCompatActivity {

    final Map<String, Map<String, Map<String, Map<String, String>>>> dat = new HashMap<String, Map<String, Map<String, Map<String, String>>>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        Spinner qdropdown = (Spinner)findViewById(R.id.quarter_spinner);
        final ArrayAdapter<String> qadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        qdropdown.setAdapter(qadapter);

        final Spinner sdropdown = (Spinner)findViewById(R.id.subject_spinner);
        final ArrayAdapter<String> sadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        sdropdown.setAdapter(sadapter);

        final Spinner cdropdown = (Spinner)findViewById(R.id.course_spinner);
        final ArrayAdapter<String> cadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        cdropdown.setAdapter(cadapter);


        Spinner idropdown = (Spinner)findViewById(R.id.instr_spinner);
        final ArrayAdapter<String> iadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        idropdown.setAdapter(iadapter);



        sdropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final String  mselection = sdropdown.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), "selected "+ mselection, Toast.LENGTH_SHORT).show();

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
                Toast.makeText(getApplicationContext(), "selected "+ mselection, Toast.LENGTH_SHORT).show();

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

        //TODO: add a function to filter menu items, at the moment, whenever the db is updated,
        //TODO: everything is repopulated automatically

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
