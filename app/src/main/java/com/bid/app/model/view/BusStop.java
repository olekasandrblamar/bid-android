package com.bid.app.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class BusStop {

    @JsonProperty("bus_stop")
    String busStop;

    @JsonProperty("eta")
    List<String> eta;


    public String getBusStop() {
        return busStop;
    }

    public List<String> getEta() {
        return eta;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
