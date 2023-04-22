package com.bid.app.model.request;

import androidx.annotation.NonNull;

import com.bid.app.model.view.DeviceDetail;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class LoginRequest implements Serializable {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("type")
    private String type;

    @JsonProperty("device_details")
    private DeviceDetail deviceDetails;

    public DeviceDetail getDeviceDetails() {
        return deviceDetails;
    }

    public void setDeviceDetails(DeviceDetail deviceDetails) {
        this.deviceDetails = deviceDetails;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
