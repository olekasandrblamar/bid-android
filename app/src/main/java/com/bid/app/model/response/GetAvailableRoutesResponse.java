package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.AvailableRoute;
import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class GetAvailableRoutesResponse implements Serializable {
    @JsonProperty("error")
    private ErrorStatus errorStatus;

    @JsonProperty("routes")
    private List<AvailableRoute> routes;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public List<AvailableRoute> getRoutes() {
        return routes;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
