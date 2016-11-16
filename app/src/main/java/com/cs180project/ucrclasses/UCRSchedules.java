package com.cs180project.ucrclasses;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by aricohen on 11/15/16.
 */

public final class UCRSchedules {

    private final static Vector<Vector<UCRCourse>> schedules = new Vector<Vector<UCRCourse>>(1);

    private UCRSchedules() { }

    //type = 0, display with add button
    //type = 1, display with remove button
    public static void displayCourse(final UCRCourse course, int type, Context context) {
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
                    schedules.get(0).remove(course);
                }
            });
        } else {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    schedules.get(0).add(course);
                    //TODO Switch to the calendar view
                    //TODO there's no getActivity or startActivity from this class since it doesn't
                    //TODO inherit from fragment, so that'll be a challenge
                    //Intent intent = new Intent(getActivity(), CalendarActivity1.class);
                    //startActivity(intent);
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
