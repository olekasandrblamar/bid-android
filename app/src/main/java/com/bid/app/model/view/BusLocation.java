package com.bid.app.model.view;

import com.bid.app.model.response.SlotDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusLocation implements Serializable {


    @SerializedName("lat")
    private String lat;

    @SerializedName("lon")
    private String lon;

    @SerializedName("bus_id")
    private String bus_id;

    @SerializedName("bus_no")
    private String bus_no;


    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }

    public String getBus_id() {
        return bus_id;
    }

    public String getBus_no() {
        return bus_no;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
