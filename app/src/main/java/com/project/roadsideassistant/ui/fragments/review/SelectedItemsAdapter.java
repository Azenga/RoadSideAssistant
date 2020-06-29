package com.project.roadsideassistant.ui.fragments.review;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.roadsideassistant.R;

import java.util.List;

public class SelectedItemsAdapter extends RecyclerView.Adapter<SelectedItemsAdapter.ItemHolder> {

    private List<String> itemsIdsList;
    private FirebaseFirestore mDb;
    private String collection;

    private static final String TAG = "SelectedItemsAdapter";

    public SelectedItemsAdapter(List<String> itemsIdsList, String collection) {
        mDb = FirebaseFirestore.getInstance();

        this.collection = collection;
        this.itemsIdsList = itemsIdsList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.single_item_layout,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {

        mDb.collection(collection).document(itemsIdsList.get(position))
                .get()
                .addOnSuccessListener(documentSnapshot -> holder.itemNameTextView.setText(documentSnapshot.getString("name")))
                .addOnFailureListener(exception -> Log.e(TAG, "onBindViewHolder: getting item name", exception));
    }

    @Override
    public int getItemCount() {
        return itemsIdsList.size();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        TextView itemNameTextView;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            itemNameTextView = (TextView) itemView;
        }
    }


}
