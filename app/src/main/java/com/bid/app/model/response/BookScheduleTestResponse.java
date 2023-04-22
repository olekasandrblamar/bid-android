package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class BookScheduleTestResponse {
    @JsonProperty("error")
    private ErrorStatus errorStatus;

    private String id;
    private String created_at;
    private String updated_at;
    private String user_id;
    private String center_id;
    private String test_type_id;
    private String reg_date;
    private String from_timeslot;
    private String qr_code;
    private String status;
    private String two_day_notify;
    private String two_hour_notify;

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

    public String getTest_type_id() {
        return test_type_id;
    }

    public void setTest_type_id(String test_type_id) {
        this.test_type_id = test_type_id;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getFrom_timeslot() {
        return from_timeslot;
    }

    public void setFrom_timeslot(String from_timeslot) {
        this.from_timeslot = from_timeslot;
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

    public String getTwo_day_notify() {
        return two_day_notify;
    }

    public void setTwo_day_notify(String two_day_notify) {
        this.two_day_notify = two_day_notify;
    }

    public String getTwo_hour_notify() {
        return two_hour_notify;
    }

    public void setTwo_hour_notify(String two_hour_notify) {
        this.two_hour_notify = two_hour_notify;
    }

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(ErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
    }


    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
