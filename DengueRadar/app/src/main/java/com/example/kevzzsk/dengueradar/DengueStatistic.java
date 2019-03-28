package com.example.kevzzsk.dengueradar;

import com.google.firebase.firestore.IgnoreExtraProperties;


@IgnoreExtraProperties

public class DengueStatistic{

    private int number;
    private String date;

    public DengueStatistic(){

    }

    public DengueStatistic(int number, String date) {
        this.number = number;
        this.date = date;
    }

    public int getNumber() {
        return number;
    }

    public String getDate() {
        return date;
    }

    public void setNumber(int num){
        number = num;
    }
    public void setDate(String date){
        this.date = date;
    }
}
