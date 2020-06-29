package com.project.roadsideassistant.data.repositories;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.roadsideassistant.data.models.Message;

public class MessageRepository {

    private FirebaseFirestore mDatabase;
    private MessageTaskListener listener;

    public MessageRepository(MessageTaskListener listener) {
        this.listener = listener;
        mDatabase = FirebaseFirestore.getInstance();
    }

    public void add(Message message) {
        mDatabase.collection("messages")
                .add(message)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        listener.onComplete("Request Sent Successfully");
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }


    public interface MessageTaskListener {

        void onComplete(String message);

        void onError(Exception exception);
    }
}
