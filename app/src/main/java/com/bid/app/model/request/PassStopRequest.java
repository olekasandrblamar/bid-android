package com.bid.app.model.request;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class PassStopRequest implements Serializable {

    @JsonProperty("pass_stop")
    private String passStop;

    public String getPassStop() {
        return passStop;
    }

    public void setPassStop(String passStop) {
        this.passStop = passStop;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
