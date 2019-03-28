package com.example.kevzzsk.dengueradar;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DatabaseViewer {

    private boolean queryTips;
    private ArrayList<Tip> tipsArray = new ArrayList<>();
    private ArrayList<DengueStatistic> statsArray = new ArrayList<>();

    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private CollectionReference TipReference =db.collection("Tips");
    private CollectionReference StatsReference = db.collection("Statistics");

    public DatabaseViewer(boolean queryTips, Object object){
        this.queryTips = queryTips;
        if(queryTips){
            loadTips((TipsInterface)object);
        }
        else {
            loadStats((StatisticInterface)object);
        }
    }

    public void loadTips (final TipsInterface tipsInterface){
        tipsArray.clear();
        TipReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Tip tip = new Tip(document.getId());
                        tip.content = document.get("Content").toString();
                        tip.img = document.get("Image").toString();

                        //test
//                        Log.d("tip", tip.heading + "/////" + tip.img + "\n");

                        tipsArray.add(tip);
                    }
                    tipsInterface.finishLoadingTips(tipsArray);

                } else {
                    //Toast.makeText(DatabaseViewer.this, "query fail check log", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loadStats (final StatisticInterface statisticInterface){
        statsArray.clear();
        StatsReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        DengueStatistic weeklyStatistic = new DengueStatistic(Integer.parseInt(document.get("NumberOfCase").toString()), document.getId());
                        statsArray.add(weeklyStatistic);
                    }
                    statisticInterface.finishLoadingStats(statsArray);

                } else {
                    //Toast.makeText(DatabaseViewer.this, "query fail check log", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

}




