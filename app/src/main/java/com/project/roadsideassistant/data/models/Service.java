package com.project.roadsideassistant.data.models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Service implements Serializable {

    @DocumentId
    private String id;

    private String name;
    private String description;

    @Exclude
    private boolean isChecked;

    public Service() {
    }

    public Service(String name, String description) {
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
        return "Service{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }

    public String getId() {
        return id;
    }
}
