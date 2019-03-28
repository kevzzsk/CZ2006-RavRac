package com.example.kevzzsk.dengueradar;

import com.google.errorprone.annotations.Var;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties

public class Statistics{
    public Object NumberOfCase;
    public String date;
    public String week;


    public Statistics() {
    }

    public Statistics(Long numberOfCase) {
        NumberOfCase = numberOfCase;
    }
}
