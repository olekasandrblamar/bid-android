package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class StatusResponse implements Serializable {

    @JsonProperty("error")
    private ErrorStatus error;

    public ErrorStatus getError() {
        return error;
    }

    public void setError(ErrorStatus error) {
        this.error = error;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
