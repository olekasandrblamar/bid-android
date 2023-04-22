package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.BusLocation;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Ticket;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class TowardBusesResponse implements Serializable {

    @JsonProperty("locations")
    private List<BusLocation> locations;

    @JsonProperty("error")
    private ErrorStatus errorStatus;

    public List<BusLocation> getLocations() {
        return locations;
    }



    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(ErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
