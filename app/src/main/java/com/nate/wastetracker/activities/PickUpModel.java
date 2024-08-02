package com.nate.wastetracker.activities;

public class PickUpModel {
    private String id;
    private String status;
    private String date;
    private String houseNo;


    public PickUpModel(String id, String status, String date) {
        this.id = id;
        this.status = status;
        this.date = date;
    }

    public PickUpModel(String id, String status, String date, String houseNo) {
        this.id = id;
        this.status = status;
        this.date = date;
        this.houseNo = houseNo;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }
}
