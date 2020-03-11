package com.project.roadsideassistant.data.models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Notification implements Serializable {

    @DocumentId
    private String id;

    private String title;
    private String content;
    private boolean isRead;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
