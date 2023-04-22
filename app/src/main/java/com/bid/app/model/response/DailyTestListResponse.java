package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.DailyTest;
import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class DailyTestListResponse {

    @JsonProperty("error")
    private ErrorStatus errorStatus;

    @JsonProperty("data")
    private List<DailyTest> dailyTestList;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(ErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
    }

    public List<DailyTest> getDailyTestList() {
        return dailyTestList;
    }

    public void setDailyTestList(List<DailyTest> dailyTestList) {
        this.dailyTestList = dailyTestList;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
