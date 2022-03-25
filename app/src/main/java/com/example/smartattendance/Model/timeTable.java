package com.example.smartattendance.Model;

public class timeTable {

    String subjectName, time , imageUrl;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public timeTable() {
    }

    public timeTable(String subjectName, String time, String imageUrl) {
        this.subjectName = subjectName;
        this.time = time;
        this.imageUrl = imageUrl;
    }
}
