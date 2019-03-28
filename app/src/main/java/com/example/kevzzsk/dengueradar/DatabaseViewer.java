package com.example.kevzzsk.dengueradar;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseViewer extends AppCompatActivity {


    private ArrayList<Tips> TipsArray = new ArrayList<>();
    private ArrayList<Statistics> StatsArray = new ArrayList<>();
    String dataParsed = "";

    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private CollectionReference TipReference =db.collection("Tips");
    private CollectionReference StatsReference = db.collection("Statistics");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public ArrayList<Tips> loadTips (View v){

        TipReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count =0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Tips tips = document.toObject(Tips.class);
                        TipsArray.add(tips);
                        dataParsed = dataParsed + TipsArray.get(count).Content.toString()+"\n";
                        count++;

                    }
                    Log.d("MAINACTIVITY",dataParsed);


                } else {
                    Toast.makeText(DatabaseViewer.this, "query fail check log", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return TipsArray;
    }

    public  ArrayList<Statistics> loadStats (){

        StatsReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count =0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Statistics stats = document.toObject(Statistics.class);
                        StatsArray.add(stats);
                        dataParsed = dataParsed + StatsArray.get(count).NumberOfCase.toString()+"\n";
                        count++;

                    }
                    Log.d("MAINACTIVITY",dataParsed);

                } else {
                    Toast.makeText(DatabaseViewer.this, "query fail check log", Toast.LENGTH_SHORT).show();
                }


            }
        });
        return StatsArray;
    }

}




