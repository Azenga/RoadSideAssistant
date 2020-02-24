package com.project.roadsideassistant.data.repositories;

import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.roadsideassistant.data.models.Service;

import java.util.ArrayList;
import java.util.List;

public class ServicesRepository {

    private static final String TAG = "ServicesRepository";

    private OnServicesTaskComplete taskListener;

    private FirebaseFirestore mDatabase;

    public ServicesRepository(OnServicesTaskComplete taskListener) {

        this.taskListener = taskListener;

        mDatabase = FirebaseFirestore.getInstance();
    }

    public void getServices() {

        mDatabase.collection("services")
                .addSnapshotListener(
                        (queryDocumentSnapshots, e) -> {
                            if (e != null) {
                                Log.e(TAG, "getServices: Failed", e);
                                assert taskListener != null;
                                taskListener.showError(e.getLocalizedMessage());
                                return;
                            }

                            Log.d(TAG, "getServices: " + queryDocumentSnapshots.toString());

                            Log.d(TAG, "getServices: successful am about to show em => count: " + queryDocumentSnapshots.size());

                            taskListener.showServices(queryDocumentSnapshots.toObjects(Service.class));
                        }
                );
    }

    public interface OnServicesTaskComplete {
        void showServices(List<Service> services);

        void showError(String message);
    }
}
