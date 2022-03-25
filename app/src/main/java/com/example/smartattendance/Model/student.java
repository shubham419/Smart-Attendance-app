package com.example.smartattendance.Model;

public class student {
    String name;
    String seatNumber;
    String uid;




    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public student() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public student(String name, String seatNumber, String uid) {
        this.name = name;
        this.seatNumber = seatNumber;
        this.uid = uid;
    }

    public student(String name, String seatNumber) {
        this.name = name;
        this.seatNumber = seatNumber;
    }
}
