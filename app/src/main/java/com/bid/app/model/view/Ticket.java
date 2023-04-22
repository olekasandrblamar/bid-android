package com.bid.app.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class Ticket implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("pick_up_time")
    private String pickUpTime;

    @JsonProperty("get_off_time")
    private String getOffTime;

    @JsonProperty("pick_date")
    private String pickDate;

    @JsonProperty("trip_id")
    private String tripId;

    @JsonProperty("distance")
    private String distance;

    @JsonProperty("polyline")
    private String polyline;

    @JsonProperty("driver_id")
    private String driverId;

    @JsonProperty("start_stop")
    private String startStop;

    @JsonProperty("start_stop_location")
    private LatLngPosition startStopLocation;

    @JsonProperty("end_stop")
    private String endStop;

    @JsonProperty("end_stop_location")
    private LatLngPosition endStopLocation;

    @JsonProperty("fare")
    private String farePrice;

    @JsonProperty("bus_no")
    private String busNo;

    @JsonProperty("bus_id")
    private String busId;

    @JsonProperty("mode")
    private String mode;

    @JsonProperty("seat_no")
    private String seatNumber;

    @JsonProperty("rating")
    private String driverRating;

    @JsonProperty("is_rated")
    private String isRated;

    @JsonProperty("route_id")
    private String routeId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("rating_cnt")
    private String driverRatingCnt;

    @JsonProperty("eta")
    private String eta;

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public LatLngPosition getEndStopLocation() {
        return endStopLocation;
    }

    public LatLngPosition getStartStopLocation() {
        return startStopLocation;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getPolyline() {
        return polyline;
    }

    public String getDistance() {
        return distance;
    }

    public String getMode() {
        return mode;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getDriverId() {
        return driverId;
    }

    public String getStartStop() {
        return startStop;
    }

    public String getEndStop() {
        return endStop;
    }

    public String getFarePrice() {
        return farePrice;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getDriverRating() {
        return driverRating;
    }

    public String getIsRated() {
        return isRated;
    }

    public String getDriverRatingCnt() {
        return driverRatingCnt;
    }

    public String getGetOffTime() {
        return getOffTime;
    }

    public String getPickDate() {
        return pickDate;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
