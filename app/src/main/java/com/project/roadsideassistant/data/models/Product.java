package com.project.roadsideassistant.data.models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Product implements Serializable {

    @DocumentId
    private String id;

    @Exclude
    private boolean isChecked;

    private String name;
    private String description;

    public Product() {
        isChecked = false;
    }

    public Product(String name, String description) {
        this.name = name;
        this.description = description;
        isChecked = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", isChecked=" + isChecked +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }
}
