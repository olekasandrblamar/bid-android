package com.bid.app.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class CheckFaceResponseData implements Serializable {
    @JsonProperty("checkResult")
    private Boolean checkResult;

    public Boolean getCheckResult() {
        return checkResult;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
