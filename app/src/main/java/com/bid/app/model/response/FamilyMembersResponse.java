package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.AvailableRoute;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.FamilyData;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class FamilyMembersResponse {
    @JsonProperty("error")
    private ErrorStatus errorStatus;

    @JsonProperty("data")
    private List<FamilyData> data;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public List<FamilyData> getData() {
        return data;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
