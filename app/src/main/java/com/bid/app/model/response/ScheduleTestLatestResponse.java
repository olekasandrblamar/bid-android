package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.CovidResult;
import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ScheduleTestLatestResponse {
    @JsonProperty("error")
    private ErrorStatus errorStatus;

    /*@JsonProperty("data")
    private TestLatest testLatests;*/

    public CovidResult getData() {
        return data;
    }

    public void setData(CovidResult data) {
        this.data = data;
    }

    @JsonProperty("data")
    private CovidResult data;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(ErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
    }


    /*public TestLatest getTestLatests() {
        return testLatests;
    }

    public void setTestLatests(TestLatest testLatests) {
        this.testLatests = testLatests;
    }*/

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
