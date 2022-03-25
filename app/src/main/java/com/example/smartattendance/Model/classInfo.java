package com.example.smartattendance.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class classInfo {
    String name, subject;
    String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public classInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

//    public classInfo(String name, String subject) {
//        this.name = name;
//        this.subject = subject;
//    }

    public classInfo(String name, String subject, String time) {
        this.name = name;
        this.subject = subject;
        this.time = time;
    }
}
