package com.bid.app.model.view;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class Step implements Serializable {
    @JsonProperty("mode")
    String mode;

    @JsonProperty("distance")
    String distance;

    @JsonProperty("to")
    String to;

    @JsonProperty("polyline")
    String polyline;

    @JsonProperty("route_id")
    String routeId;

    public String getTo() {
        return to;
    }

    public String getDistance() {
        return distance;
    }

    public String getMode() {
        return mode;
    }

    public String getPolyline() {
        return polyline;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }


    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
