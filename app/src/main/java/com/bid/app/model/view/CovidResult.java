package com.bid.app.model.view;

import androidx.annotation.NonNull;

import com.bid.app.model.response.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;


public class CovidResult {

    private String id;

    private String created_at;

    private String updated_at;

    private String user_id;

    private String center_id;

    private String reg_date;

    private String qr_code;

    private String status;

    private Center center;

    private String roam_free_start;

    private String roam_free_end;

    private TestType test_type;

    private User user;

    private TestStatus test_status;

    @JsonProperty("address")
    private Address address;

    public TestType getTest_type() {
        return test_type;
    }

    public void setTest_type(TestType test_type) {
        this.test_type = test_type;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCenter_id() {
        return center_id;
    }

    public void setCenter_id(String center_id) {
        this.center_id = center_id;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }


    public TestStatus getTest_status() {
        return test_status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRoam_free_start() {
        return roam_free_start;
    }

    public void setRoam_free_start(String roam_free_start) {
        this.roam_free_start = roam_free_start;
    }

    public String getRoam_free_end() {
        return roam_free_end;
    }

    public void setRoam_free_end(String roam_free_end) {
        this.roam_free_end = roam_free_end;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setTest_status(TestStatus test_status) {
        this.test_status = test_status;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
