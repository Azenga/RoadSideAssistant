package com.project.roadsideassistant.data.models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {

    @DocumentId
    private String id;

    private String carModel;
    private String userId;
    private String locationName;

    private List<String> productsList;
    private List<String> servicesList;

    private double lat;
    private double lng;
    private String carPlateNumber;
    private String description;
    private String garage;
    private boolean read;

    public Message() {
        productsList = new ArrayList<>();
        servicesList = new ArrayList<>();
    }

    public Message(String carModel, String userId, boolean read) {
        this.carModel = carModel;
        this.userId = userId;
        this.read = read;

        productsList = new ArrayList<>();
        servicesList = new ArrayList<>();
    }

    //Getters
    public String getId() {
        return id;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getLocationName() {
        return locationName;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getCarPlateNumber() {
        return carPlateNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getGarage() {
        return garage;
    }

    public boolean isRead() {
        return read;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getProductsList() {
        return productsList;
    }

    public List<String> getServicesList() {
        return servicesList;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setCarPlateNumber(String carPlateNumber) {
        this.carPlateNumber = carPlateNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGarage(String garage) {
        this.garage = garage;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setLocation(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public void addService(String serviceId) {
        servicesList.add(serviceId);
    }

    public void addProduct(String productId) {
        productsList.add(productId);
    }

    public void addProducts(List<Product> products) {
        for (Product product : products) {
            productsList.add(product.getId());
        }
    }

    public void addServices(List<Service> services) {
        for (Service service : services) {
            servicesList.add(service.getId());
        }
    }

}
