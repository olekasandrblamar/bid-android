package com.bid.app.model.view;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class CurrentPos {
    private String lat;

    private String lon;

    public Double getLat() {
        return new Double(lat);
    }

//    public void setLat(String lat) {
//        this.lat = lat;
//    }

    public Double getLon() {
        return new Double(lon);
    }

//    public void setLon(String lon) {
//        this.lon = lon;
//    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
