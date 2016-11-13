package com.cs180project.ucrclasses;

/**
 * Created by aricohen on 11/12/16.
 */

public class UCRCourse {
    public String availableSeats;
    public String maxEnrollment;
    public String numOnWaitlist;
    public String maxWaitlist;
    public String buildingName;
    public String callNum;
    public String catalogDescription;
    public String coreqs;
    public String courseNum;
    public String courseTitle;
    public String days;
    public String finalExamDate;
    public String finalExamTime;
    public String instructor;
    public String courseType;
    public String prereqs;
    public String restrictions;
    public String room;
    public String subject;
    public String time;
    public String units;

    public UCRCourse(String as, String me, String nowl, String mwl, String bn, String calln, String cd,
                     String cr, String coursen, String ctitle, String d, String fed, String fet,
                     String i, String ctype, String pr, String restr, String ro, String sub, String t, String u) {
        availableSeats = as;
        maxEnrollment = me;
        numOnWaitlist = nowl;
        maxWaitlist = mwl;
        buildingName = bn;
        callNum = calln;
        catalogDescription = cd;
        coreqs = cr;
        courseNum = coursen;
        courseTitle = ctitle;
        days = d;
        finalExamDate = fed;
        finalExamTime = fet;
        instructor = i;
        courseType = ctype;
        prereqs = pr;
        restrictions = restr;
        room = ro;
        subject = sub;
        time = t;
        units = u;
    }

}
