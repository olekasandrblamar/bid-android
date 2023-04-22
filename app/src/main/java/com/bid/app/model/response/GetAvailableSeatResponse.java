package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

@JsonPropertyOrder({"name", "card_number", "expiry_date", "cvv"})
public class GetAvailableSeatResponse implements Serializable {

    @JsonProperty("available_seat")
    private List<String> availableSeat;

    @JsonProperty("error")
    private ErrorStatus errorStatus;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public List<String> getAvailableSeat() {
        return availableSeat;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
