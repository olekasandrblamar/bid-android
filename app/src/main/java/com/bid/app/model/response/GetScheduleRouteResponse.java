package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.CurrentPos;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Schedule;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class GetScheduleRouteResponse {
    @JsonProperty("schedules")
    private List<Schedule> schedules;

    @JsonProperty("error")
    private ErrorStatus errorStatus;

    public List<Schedule> getSchedules(){
        return schedules;
    }

    public  ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }


}
