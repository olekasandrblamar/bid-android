package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.FamilyData;
import com.bid.app.model.view.Profile;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class FindSomeoneWithMobileResponse {
    @JsonProperty("error")
    private ErrorStatus errorStatus;

    @JsonProperty("data")
    private Profile data;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public Profile getData() {
        return data;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
