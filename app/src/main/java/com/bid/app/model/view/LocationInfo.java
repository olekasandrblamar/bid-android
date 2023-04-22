package com.bid.app.model.view;

import com.bid.app.model.response.SlotDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.Map;

public class LocationInfo {

    @SerializedName("location_id")
    @Expose
    private String location_id;

    @SerializedName("location_name")
    @Expose
    private String location_name;

    @SerializedName("timeslots")
    @Expose
    ArrayList<Map<String, SlotDetails>> timeslots;

    @SerializedName("test_types")
    @Expose
    private ArrayList<TestType> test_types;

    public ArrayList<TestType> getTest_types() {
        return test_types;
    }

    public void setTest_types(ArrayList<TestType> test_types) {
        this.test_types = test_types;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public ArrayList<Map<String, SlotDetails>> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(ArrayList<Map<String, SlotDetails>> timeslots) {
        this.timeslots = timeslots;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
