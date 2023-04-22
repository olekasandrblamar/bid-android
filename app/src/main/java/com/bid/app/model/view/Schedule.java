package com.bid.app.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class Schedule implements Serializable {
    @JsonProperty("bus_id")
    private String busId;
    @JsonProperty("route_name")
    private String routeName;
    @JsonProperty("start_stop")
    private String startStop;
    @JsonProperty("start_time")
    private String startTime;
    //List<BusTimeRating>
    @JsonProperty("end_stop")
    private String endStop;
    @JsonProperty("end_time")
    private String endTime;
    @JsonProperty("eta")
    private String eta;
    @JsonProperty("fare_price")
    private String farePrice;

    public String getBusId() { return busId;}
    public String getRouteName() { return routeName;}
    public String getStartStop() { return startStop;}
    public String getStartTime() { return startTime;}
    public String getEndStop() { return endStop;}
    public String getEndTime() { return endTime;}
    public String getEta() { return eta;}
    public String getFarePrice() { return farePrice;}
    public void setBusId(String busId) {
        this.busId = busId;
    }

    public void setEndStop(String endStop) {
        this.endStop = endStop;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public void setFarePrice(String farePrice) {
        this.farePrice = farePrice;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public void setStartStop(String startStop) {
        this.startStop = startStop;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
