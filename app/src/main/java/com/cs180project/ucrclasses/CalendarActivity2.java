package com.cs180project.ucrclasses;

import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity2 extends BaseCalendarActivity {

    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Calendar startTime = Calendar.getInstance();
        Calendar endTime = (Calendar) startTime.clone();
        WeekViewEvent event;

        if(!UCRSchedules.isEmpty(1)) {
//            Log.d("NOTHING", "Not Empty");
            Log.d("NOTHING", "Schedule Size = " + Integer.toString(UCRSchedules.getSize(1)));
            for (int i = 0; i < UCRSchedules.getSize(1); i++) {
                for (int j = 0; j < UCRSchedules.getDays(1, i).length(); j++) {
                    startTime = Calendar.getInstance();
                    startTime.set(Calendar.HOUR_OF_DAY, UCRSchedules.getStartHour(1, i));
                    startTime.set(Calendar.MINUTE, UCRSchedules.getStartMin(1, i));
                    startTime.set(Calendar.MONTH, newMonth - 1);    //keep the same or will schedule multiple times

//                    Log.d("NOTHING", "Hour start = " + Integer.toString(UCRSchedules.getStartHour(0, i)));
//                    Log.d("NOTHING", "Minute start = " + Integer.toString(UCRSchedules.getStartMin(0, i)));

                    Log.d("NOTHING", UCRSchedules.getDays(1, i));
                    if (UCRSchedules.getDays(1, i).charAt(j) == 'M')
                        startTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    else if (UCRSchedules.getDays(1, i).charAt(j) == 'T')
                        startTime.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                    else if (UCRSchedules.getDays(1, i).charAt(j) == 'W')
                        startTime.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                    else if (UCRSchedules.getDays(1, i).charAt(j) == 'R')
                        startTime.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                    else if (UCRSchedules.getDays(1, i).charAt(j) == 'F')
                        startTime.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                    else {
                        Log.d("ERROR", "Invalid day: " + UCRSchedules.getDays(1, i).charAt(j) + " for call number: " + UCRSchedules.getCallNum(1, i));
                        startTime.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    }

                    endTime = (Calendar) startTime.clone();
                    endTime.add(Calendar.HOUR_OF_DAY, (UCRSchedules.getEndHour(1, i) - UCRSchedules.getStartHour(1, i)) );
                    endTime.add(Calendar.MINUTE, UCRSchedules.getEndMin(1, i) - UCRSchedules.getStartMin(1, i));
                    endTime.set(Calendar.MONTH, newMonth - 1);  //keep the same or will schedule multiple times

//                    Log.d("END HOUR1", Integer.toString((UCRSchedules.getEndHour(0, i))));
//                    Log.d("END HOUR2", Integer.toString((UCRSchedules.getStartHour(0, i))));
//                    Log.d("END HOUR", Integer.toString((UCRSchedules.getEndHour(0, i) - UCRSchedules.getStartHour(0, i))));
//                    Log.d("END MIN", Integer.toString(UCRSchedules.getEndMin(0, i) - UCRSchedules.getStartMin(0, i)));

                    Log.d("NOTHING", UCRSchedules.getCourseNum(1, i) +" "+ UCRSchedules.getCourseType(1, i));
                    event = new WeekViewEvent(Integer.parseInt(UCRSchedules.getCallNum(1, i)), UCRSchedules.getCourseNum(1, i) + " " + UCRSchedules.getCourseType(1, i), startTime, endTime);
                    switch (i%4){
                        case 0:
                            event.setColor(getResources().getColor(R.color.event_color_01));
                            break;
                        case 1:
                            event.setColor(getResources().getColor(R.color.event_color_02));
                            break;
                        case 2:
                            event.setColor(getResources().getColor(R.color.event_color_03));
                            break;
                        case 3:
                            event.setColor(getResources().getColor(R.color.event_color_04));
                            break;
                    }
                    events.add(event);
                }
            }
        }


        /* //example runs
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 8);
        startTime.set(Calendar.MINUTE, 10);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 1);
        endTime.add(Calendar.MINUTE, -10);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(1, "CS170", startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 11);
        startTime.set(Calendar.MINUTE, 10);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 1);
        endTime.add(Calendar.MINUTE, 20);
        endTime.set(Calendar.MONTH, newMonth - 1);
        event = new WeekViewEvent(3, "CS 180", startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);
*/
        return events;
    }

}