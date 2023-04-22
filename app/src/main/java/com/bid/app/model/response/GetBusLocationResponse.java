package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.CurrentPos;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Location;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class GetBusLocationResponse implements Serializable {
    @JsonProperty("error")
    private ErrorStatus errorStatus;

    @JsonProperty("data")
    private CurrentPos currentPos;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public CurrentPos getCurrentPos() {
        return currentPos;
    }


    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
