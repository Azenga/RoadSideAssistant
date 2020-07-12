package com.project.roadsideassistant.data.repositories;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.roadsideassistant.data.models.Notification;

import java.util.List;

public class NotificationRepository {

    private NotificationTaskListener listener;
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    public NotificationRepository(NotificationTaskListener listener) {
        this.listener = listener;
    }

    public void getByUserId(String userId) {
        mDb.collection("users").document(userId).collection("notifications")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        listener.onError(e);
                    }

                    assert queryDocumentSnapshots != null;

                    listener.onGetAll(queryDocumentSnapshots.toObjects(Notification.class));
                });
    }


    public interface NotificationTaskListener {
        void onGetAll(List<Notification> notifications);

        void onError(Exception e);
    }
}
