package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.CheckFaceResponseData;
import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class CheckFaceResponse implements Serializable {

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private CheckFaceResponseData data;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public CheckFaceResponseData getData() {
        return data;
    }


    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
