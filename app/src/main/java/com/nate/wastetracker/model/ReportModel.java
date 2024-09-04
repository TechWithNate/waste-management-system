package com.nate.wastetracker.model;

public class ReportModel {
    private String email;
    private String houseNo;
    private String date;
    private String message;

    public ReportModel(String email, String houseNo, String date, String message) {
        this.email = email;
        this.houseNo = houseNo;
        this.date = date;
        this.message = message;
    }

    public ReportModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
