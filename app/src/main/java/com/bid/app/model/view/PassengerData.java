package com.bid.app.model.view;

import com.bid.app.model.response.GetRouteByBusResponse;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class PassengerData implements Serializable {

    String firstName;
    String lastName;
    String mobile;
    String seatNumber;
    String bid;

    GetRouteByBusResponse getRouteByBusResponse;

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBid() {
        return bid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setGetRouteByBusResponse(GetRouteByBusResponse getRouteByBusResponse) {
        this.getRouteByBusResponse = getRouteByBusResponse;
    }

    public GetRouteByBusResponse getGetRouteByBusResponse() {
        return getRouteByBusResponse;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
