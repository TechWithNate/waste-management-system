package com.nate.wastetracker.activities;

public class PickUpModel {
    private String id;
    private String status;

    public PickUpModel(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public PickUpModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
