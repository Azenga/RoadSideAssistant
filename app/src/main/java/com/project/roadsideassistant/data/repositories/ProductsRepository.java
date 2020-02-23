package com.project.roadsideassistant.data.repositories;

import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.roadsideassistant.data.models.Product;

import java.util.ArrayList;
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

                    List<Product> products = new ArrayList<>();

                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        products.add(documentChange.getDocument().toObject(Product.class));
                    }

                    taskListener.showProducts(products);

                });
    }

    public interface ProductTaskListener {

        void showProducts(List<Product> products);

        void showError(String message);
    }
}
