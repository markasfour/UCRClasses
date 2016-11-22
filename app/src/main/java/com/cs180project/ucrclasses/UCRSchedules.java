package com.cs180project.ucrclasses;

import android.app.Activity;
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
    public static int activeSched = 0;

    private UCRSchedules() { }

    private static int initRun = 0;
    public static void scheduleInit(){
        if(initRun == 0) {
            Vector<UCRCourse> temp = new Vector<UCRCourse>(0);
            Vector<UCRCourse> temp2 = new Vector<UCRCourse>(0);
            Vector<UCRCourse> temp3 = new Vector<UCRCourse>(0);
            schedules.add(temp);
            schedules.add(temp2);
            schedules.add(temp3);
            initRun = 1;
        }
    }

    public static void addCourse(UCRCourse course, int schedNum) {
        if(!schedules.elementAt(schedNum).contains(course)) {
            schedules.elementAt(schedNum).add(course);
        }
    }

    public static void removeCourse(UCRCourse course, int schedNum) {
        if(schedules.elementAt(schedNum).contains(course)) {
            schedules.elementAt(schedNum).remove(course);
        }
    }

    public static void clearall(){
        for(int i = 0; i < 3; i++){
            schedules.elementAt(i).clear();
        }
    }

    public static boolean isEmpty(int schedNum){
        return schedules.elementAt(schedNum).isEmpty();
    }

    public static int getSize(int schedNum){
        return schedules.elementAt(schedNum).size();
    }

    public static int getSize() { return schedules.size(); }

    public static UCRCourse getCourse(int schedNum, int classNum) {
        return schedules.elementAt(schedNum).elementAt(classNum);
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
        return getStartHour(schedules.elementAt(schedNum).elementAt(classNum));
    }

    public static int getStartHour(UCRCourse course){
        String time = course.time;
        if(!goodTime(time)) return 0;
        int itime = 0;
        if(time.charAt(6) == 'A' || time.substring(0, 2).equals("12")){
            itime = Integer.parseInt(time.substring(0, 2));
        }
        else if(time.charAt(6) == 'P'){
            itime =  Integer.parseInt(time.substring(0, 2)) + 12;
        }
        return itime;
    }

    public static int getStartMin(int schedNum, int classNum){
        return getStartMin(schedules.elementAt(schedNum).elementAt(classNum));
    }

    public static int getStartMin(UCRCourse course) {
        String time = course.time;
        if(!goodTime(time)) return 0;
        //Log.d("Start call num", course.callNum);
        //Log.d("Start subject", course.subject);
        //Log.d("START MIN", time);
        return Integer.parseInt(time.substring(3, 5));
    }

    public static int getEndHour(int schedNum, int classNum){
        return getEndHour(schedules.elementAt(schedNum).elementAt(classNum));
    }

    public static int getEndHour(UCRCourse course){
        String time = course.time;
        if(!goodTime(time)) return 0;
        int itime = 0;

        if(time.charAt(17) == 'A' || time.substring(11, 13).equals("12") ) {
            itime = Integer.parseInt(time.substring(11, 13));
        }
        else if(time.charAt(17) == 'P'){
            itime =  Integer.parseInt(time.substring(11, 13)) + 12;
        }
        return itime;
    }

    public static int getEndMin(int schedNum, int classNum){
        return getEndMin(schedules.elementAt(schedNum).elementAt(classNum));
    }

    public static int getEndMin(UCRCourse course){
        String time = course.time;
        if(!goodTime(time)) return 0;
        return Integer.parseInt(time.substring(14, 16));
    }

    public static boolean goodTime(String time) {
        if(time.length() == 0 || time.charAt(0) < '0' || time.charAt(0) > '9') return false;
        return true;
    }

    //type = 0, display with add button
    //type = 1, display with remove button
    public static void displayCourse(final UCRCourse course, int type, final Context context, final int schedNum) {
        final Dialog dialog = new Dialog(context);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.class_popup);
        //dialog.setTitle(((UCRCourse)ladapter.getItem(position)).courseNum);

        // set the custom dialog components - text, image and button
        Button addButton = (Button) dialog.findViewById(R.id.dialogButtonADD);

        TextView courseTitleText = (TextView) dialog.findViewById(R.id.popup_classTitle);
        courseTitleText.setText(course.courseTitle);

        TextView titleText = (TextView) dialog.findViewById(R.id.popup_title);
        titleText.setText(course.courseNum);

        TextView callnoText = (TextView) dialog.findViewById(R.id.popup_callno);
        callnoText.setText(course.callNum);

        TextView typeText = (TextView) dialog.findViewById(R.id.popup_classtype);
        typeText.setText(course.courseType);

        TextView timesText = (TextView) dialog.findViewById(R.id.popup_times);
        if (course.days.equals("n/a") || !UCRSchedules.goodTime(course.time)) {
            timesText.setText("n/a");
            addButton.setEnabled(false);
        } else
            timesText.setText(course.days + ": " + course.time);

        TextView instrText = (TextView) dialog.findViewById(R.id.popup_instr);
        instrText.setText(course.instructor);

        TextView seatsText = (TextView) dialog.findViewById(R.id.popup_seats);
        seatsText.setText(course.availableSeats + "/" + course.maxEnrollment);

        TextView waitlistText = (TextView) dialog.findViewById(R.id.popup_waitlist);
        waitlistText.setText(course.numOnWaitlist + "/" + course.maxWaitlist);

        TextView units = (TextView) dialog.findViewById(R.id.popup_units);
        units.setText(course.units);

        TextView finalText = (TextView) dialog.findViewById(R.id.popup_final);
        if (course.finalExamTime.equals("n/a"))
            finalText.setText("n/a");
        else
            finalText.setText(course.finalExamDate + "\n" + course.finalExamTime);

        TextView descText = (TextView) dialog.findViewById(R.id.popup_description);
        descText.setText(course.catalogDescription);

        if(type == 1) {
            addButton.setText(" Remove ");
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    removeCourse(course, schedNum);
                    Intent myIntent;
                    if(schedNum == 0) {
                        myIntent = new Intent(context, CalendarActivity1.class);
                    }
                    else if(schedNum == 1) {
                        myIntent = new Intent(context, CalendarActivity2.class);
                    }
                    else {
                        myIntent = new Intent(context, CalendarActivity3.class);
                    }
                    ((Activity)context).finish();
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(myIntent);
                }
            });
        } else {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog scheduleDialog = new Dialog(context);
                    scheduleDialog.setContentView(R.layout.schedule_select);

                    Button sched1Button = (Button) scheduleDialog.findViewById(R.id.schedule1Button);
                    Button sched2Button = (Button) scheduleDialog.findViewById(R.id.schedule2Button);
                    Button sched3Button = (Button) scheduleDialog.findViewById(R.id.schedule3Button);
                    Button schedCancelButton = (Button) scheduleDialog.findViewById(R.id.scheduleCancelButton);

                    sched1Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            scheduleDialog.dismiss();
                            addCourse(course, 0);
                            activeSched = 0;
                            Intent myIntent = new Intent(context, CalendarActivity1.class);
                            context.startActivity(myIntent);
                        }
                    });

                    sched2Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            scheduleDialog.dismiss();
                            addCourse(course, 1);
                            activeSched = 1;
                            Intent myIntent = new Intent(context, CalendarActivity2.class);
                            context.startActivity(myIntent);
                        }
                    });

                    sched3Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            scheduleDialog.dismiss();
                            addCourse(course, 2);
                            activeSched = 2;
                            Intent myIntent = new Intent(context, CalendarActivity3.class);
                            context.startActivity(myIntent);
                        }
                    });

                    schedCancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            scheduleDialog.dismiss();
                        }
                    });

                    scheduleDialog.show();
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
