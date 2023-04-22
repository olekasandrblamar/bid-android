package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class TimeSlotResponse {

    @JsonProperty("error")
    private ErrorStatus errorStatus;


    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(ErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
    }


    @JsonProperty("data")
    private List<SlotList> slot;

    public List<SlotList> getSlot() {
        return slot;
    }

    public void setSlot(List<SlotList> slot) {
        this.slot = slot;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }





}
