package com.example.kevzzsk.dengueradar;

import android.view.View;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FeedbackStore {

    public static void saveNote(String feedback, String title){
        FirebaseFirestore db =FirebaseFirestore.getInstance();
        CollectionReference FeedbackReference = db.collection("Feedback");

        Feedback userFeedback = new Feedback();
        userFeedback.UserFeedback = feedback;
        userFeedback.title = title;
        FeedbackReference.document().set(userFeedback);
    }

}
