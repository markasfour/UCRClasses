package com.cs180project.ucrclasses;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;
import java.util.Vector;

/**
 * Created by aricohen on 11/15/16.
 */

public final class UCRSchedules {

    private final static Vector<Vector<UCRCourse>> schedules = new Vector<Vector<UCRCourse>>();

    private UCRSchedules() { }

    public static void scheduleInit(){
        Vector<UCRCourse> temp = new Vector<UCRCourse>(0);
        schedules.add(temp);
        schedules.add(temp);
        schedules.add(temp);
        //TODO set a flag so this isnt run twice
    }

    public static void addCourse(UCRCourse course, int schedNum) {
        if(!schedules.elementAt(schedNum).contains(course)) {
            Log.d("NOTHING", "Adding");
            schedules.elementAt(schedNum).add(course);
        }
    }

    public static void removeCourse(UCRCourse course, int schedNum) {
        if(schedules.elementAt(schedNum).contains(course)) {
            schedules.elementAt(schedNum).remove(course);
        }
    }

    public static boolean isEmpty(int schedNum){
        return schedules.elementAt(schedNum).isEmpty();
    }

    public static int getSize(int schedNum){
        return schedules.elementAt(schedNum).size();
    }

    public static String getCourseNum(int schedNum, int classNum){
        return schedules.elementAt(schedNum).elementAt(classNum).courseNum;
    }

    public static String getDays(int schedNum, int classNum){
        return schedules.elementAt(schedNum).elementAt(classNum).days;
    }

    public static String getCourseType(int schedNum, int classNum){
        return schedules.elementAt(schedNum).elementAt(classNum).courseType;
    }

    public static String getCallNum(int schedNum, int classNum){
        return schedules.elementAt(schedNum).elementAt(classNum).callNum;
    }

    public static int getStartHour(int schedNum, int classNum){
        String time = schedules.elementAt(schedNum).elementAt(classNum).time;
        int itime = 0;
        //Log.d("CHAR", "char = " + time.charAt(6));
        Log.d("CHAR", "|" + time.substring(0,2) + "|");
        if(time.charAt(6) == 'A' || time.substring(0, 2).equals("12")){
            itime = Integer.parseInt(time.substring(0, 2));
            Log.d("START HOUR", Integer.toString(itime));
        }
        else if(time.charAt(6) == 'P'){
            itime =  Integer.parseInt(time.substring(0, 2)) + 12;
            Log.d("START HOUR", Integer.toString(itime));
        }
        return itime;
    }

    public static int getStartMin(int schedNum, int classNum){
        String time = schedules.elementAt(schedNum).elementAt(classNum).time;
        Log.d("START MIN", time.substring(3,5));
        return Integer.parseInt(time.substring(3, 5));
    }

    public static int getEndHour(int schedNum, int classNum){
        String time = schedules.elementAt(schedNum).elementAt(classNum).time;
        int itime = 0;
        Log.d("CHAR", Integer.toString(time.length()) + "|" + time + "|");
//        Log.d("END HOUR", time.substring(11, 13));

        if(time.charAt(17) == 'A' || time.substring(11, 13).equals("12") ) {
            itime = Integer.parseInt(time.substring(11, 13));
            Log.d("END HOUR", Integer.toString(itime));
        }
        else if(time.charAt(17) == 'P'){
            itime =  Integer.parseInt(time.substring(11, 13)) + 12;
            Log.d("END HOUR", Integer.toString(itime));
        }
        return itime;
    }

    public static int getEndMin(int schedNum, int classNum){
        String time = schedules.elementAt(schedNum).elementAt(classNum).time;
        Log.d("END MIN", time.substring(14,16));
        return Integer.parseInt(time.substring(14, 16));
    }

    //type = 0, display with add button
    //type = 1, display with remove button
    public static void displayCourse(final UCRCourse course, int type, final Context context, final int schedNum) {
        final Dialog dialog = new Dialog(context);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.class_popup);
        //dialog.setTitle(((UCRCourse)ladapter.getItem(position)).courseNum);

        // set the custom dialog components - text, image and button
        TextView titleText = (TextView) dialog.findViewById(R.id.popup_title);
        titleText.setText(course.courseNum);


        TextView callnoText = (TextView) dialog.findViewById(R.id.popup_callno);
        callnoText.setText(course.callNum);

        TextView typeText = (TextView) dialog.findViewById(R.id.popup_classtype);
        typeText.setText(course.courseType);


        TextView timesText = (TextView) dialog.findViewById(R.id.popup_times);
        timesText.setText(course.days + ": " + course.time);

        TextView instrText = (TextView) dialog.findViewById(R.id.popup_instr);
        instrText.setText(course.instructor);

        TextView seatsText = (TextView) dialog.findViewById(R.id.popup_seats);
        seatsText.setText(course.availableSeats + "/" + course.maxEnrollment);

        TextView waitlistText = (TextView) dialog.findViewById(R.id.popup_waitlist);
        waitlistText.setText(course.numOnWaitlist + "/" + course.maxWaitlist);

        TextView descText = (TextView) dialog.findViewById(R.id.popup_description);
        descText.setText(course.catalogDescription);


        Button addButton = (Button) dialog.findViewById(R.id.dialogButtonADD);
        if(type == 1) {
            addButton.setText(" Remove ");
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    removeCourse(course, schedNum);
                }
            });
        } else {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    // TODO Not sure how you want to store things Peter, but this is
                    // TODO breaking for some reason so it'll need to be fixed if you want to
                    // TODO use vectors like this
                    addCourse(course, schedNum);
                    Intent myIntent = new Intent(context, CalendarActivity1.class);
                    context.startActivity(myIntent);
                }
            });
        }

        // if button is clicked, close the custom dialog
        Button closeButton = (Button) dialog.findViewById(R.id.dialogButtonCLOSE);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
