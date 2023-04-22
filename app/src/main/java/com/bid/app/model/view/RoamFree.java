package com.bid.app.model.view;

import java.io.Serializable;

public class RoamFree implements Serializable {

    private String username;
    private String dob;
    private String PassportNumber;
    private String roamStart;
    private String roamEnd;
    private String status;
    private String testType;
    private String center;
    private String country;
    private String qr_code;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPassportNumber() {
        return PassportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        PassportNumber = passportNumber;
    }

    public String getRoamStart() {
        return roamStart;
    }

    public void setRoamStart(String roamStart) {
        this.roamStart = roamStart;
    }

    public String getRoamEnd() {
        return roamEnd;
    }

    public void setRoamEnd(String roamEnd) {
        this.roamEnd = roamEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }
}
