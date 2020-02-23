package com.project.roadsideassistant.data.repositories;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.roadsideassistant.data.models.Product;

import java.util.List;

public class ProductsRepository {
    private static final String TAG = "ProductsRepository";

    private FirebaseFirestore mDatabase;

    private ProductTaskListener taskListener;

    public ProductsRepository(ProductTaskListener taskListener) {
        mDatabase = FirebaseFirestore.getInstance();
        this.taskListener = taskListener;
    }

    public void getProducts() {
        mDatabase.collection("products")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {

                    if (e != null) {
                        taskListener.showError(e.getLocalizedMessage());
                        Log.e(TAG, "getProducts: Products", e);
                        return;
                    }

                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d(TAG, "getProducts: Success => count 0");
                    }

                    taskListener.showProducts(queryDocumentSnapshots.toObjects(Product.class));

                });
    }

    public interface ProductTaskListener {

        void showProducts(List<Product> products);

        void showError(String message);
    }
}
