package com.nate.wastetracker.activities;

public class Waste {
    private String id;
    private String email;
    private String message;
    private String date;
    private String day;
    private String address;
    private String type;
    private String status;
    private String wasteID;

    public Waste() {
    }

    public Waste(String id, String email, String message, String date, String day) {
        this.id = id;
        this.email = email;
        this.message = message;
        this.date = date;
        this.day = day;

    }

    public Waste(String id, String date, String day, String address, String type, String status) {
        this.id = id;
        this.date = date;
        this.day = day;
        this.address = address;
        this.type = type;
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWasteID() {
        return wasteID;
    }

    public void setWasteID(String wasteID) {
        this.wasteID = wasteID;
    }


    public void toggleStatus() {
        if ("completed".equals(status)) {
            status = "incomplete";
        } else {
            status = "completed";
        }
    }

}
