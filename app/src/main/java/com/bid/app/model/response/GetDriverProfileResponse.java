package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class GetDriverProfileResponse implements Serializable {
    @JsonProperty("error")
    private ErrorStatus errorStatus;

    @JsonProperty("data")
    private LoginResponse data;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }


    public LoginResponse getData() {
        return data;
    }


    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
