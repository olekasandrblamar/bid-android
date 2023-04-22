package com.bid.app.model.view;

import com.bid.app.model.response.SlotList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class Center {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("island_id")
    @Expose
    private String island_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("lat")
    @Expose
    private String lat;

    @SerializedName("lon")
    @Expose
    private String lon;

    @SerializedName("is_active")
    @Expose
    private String is_active;

    @SerializedName("test_types")
    @Expose
    private List<TestType> test_types;

    @SerializedName("available_slots")
    @Expose
    private List<SlotList> available_slots;

    public List<SlotList> getAvailable_slots() {
        return available_slots;
    }

    public void setAvailable_slots(List<SlotList> available_slots) {
        this.available_slots = available_slots;
    }

    public List<TestType> getTest_types() {
        return test_types;
    }

    public void setTest_types(List<TestType> test_types) {
        this.test_types = test_types;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsland_id() {
        return island_id;
    }

    public void setIsland_id(String island_id) {
        this.island_id = island_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
