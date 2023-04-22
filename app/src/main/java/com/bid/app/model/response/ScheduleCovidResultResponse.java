package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.CovidResult;
import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

@JsonPropertyOrder({"error", "data"})
public class ScheduleCovidResultResponse {

    @JsonProperty("error")
    private ErrorStatus errorStatus;

    @JsonProperty("data")
    private List<CovidResult> covidResultList;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(ErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
    }

    public List<CovidResult> getCovidResultList() {
        return covidResultList;
    }

    public void setCovidResultList(List<CovidResult> covidResultList) {
        this.covidResultList = covidResultList;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
