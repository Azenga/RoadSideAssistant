package com.project.roadsideassistant.data.models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Notification implements Serializable {

    @DocumentId
    private String id;

    private String title;
    private String body;
    private String cost;

    public Notification() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
