package com.bid.app.model.view;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class Stop implements Serializable {
    @JsonProperty("id")
    String id;

    @JsonProperty("bus_stop")
    String busStop;

    public String getId() {
        return id;
    }

    public String getBusStop() {
        return busStop;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
