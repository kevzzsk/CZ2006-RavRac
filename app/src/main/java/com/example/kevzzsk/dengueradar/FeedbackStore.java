package com.example.kevzzsk.dengueradar;

import android.view.View;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FeedbackStore {
    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private CollectionReference FeedbackReference = db.collection("Feedback");

    public void saveNote(String feedback, String title){

        feedback userFeedback = new feedback();
        userFeedback.UserFeedback = feedback;
        userFeedback.title = title;
        FeedbackReference.document().set(userFeedback);
    }



}
