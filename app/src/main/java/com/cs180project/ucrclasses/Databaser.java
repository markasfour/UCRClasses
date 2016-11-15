package com.cs180project.ucrclasses;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aricohen on 11/13/16.
 */

public final class Databaser {
    public static final Map<String, Map<String, Map<String, UCRCourse>>> dat = new HashMap<String, Map<String, Map<String, UCRCourse>>>();
    private static ValueEventListener fetcher = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            fetching = true;
            dat.clear();

            for (DataSnapshot quarters: dataSnapshot.getChildren()) {
                Map<String, Map<String, UCRCourse>> mclasses = new HashMap<String, Map<String, UCRCourse>>();
                for(DataSnapshot classes: quarters.getChildren()) {
                    Map<String, UCRCourse> mcourse_nums = new HashMap<String, UCRCourse>();

                    for(DataSnapshot callNums: classes.getChildren()) {
                        if(callNums.hasChildren()) {
                            String course = (String) callNums.child("CourseNum").getValue();
                            //Log.d("Breaking: ", "Quarter: " + quarters.getKey() + "\tSubject: " + classes.getKey() + "\tCall Num: " + callNums.getKey() + "\tCourse: " + course);
                            if(course.indexOf('-') == -1) {
                                Log.d("ERROR", "Course with call num " + callNums.getKey() + " has no dash in CourseNum: " + course);
                                continue;
                            }

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
            fetching = false;
        }

        @Override
        public void onCancelled(DatabaseError firebaseError) { }
    }; //Setup the code to repopulate dat;
    public static boolean fetching = false;

    private Databaser() { }

    public static void fetchData() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.addListenerForSingleValueEvent(fetcher);
    }


}
