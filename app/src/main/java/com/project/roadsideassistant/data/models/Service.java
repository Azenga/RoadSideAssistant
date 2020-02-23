package com.project.roadsideassistant.data.models;

public class Service {

    private String name;
    private String description;

    public Service() {
    }

    public Service(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
