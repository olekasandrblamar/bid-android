package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Ticket;
import com.bid.app.model.view.Trip;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

@JsonPropertyOrder({"upcoming", "history", "error"})
public class GetTripHistoryResponse implements Serializable {
    List<Trip> all;

    List<Trip> upcoming;

    List<Trip> history;

    ErrorStatus error;

    public List<Trip> getHistory() {
        return history;
    }

    public List<Trip> getAll() {
        return all;
    }

    public List<Trip> getUpcoming() {
        return upcoming;
    }


    public ErrorStatus getError() {
        return error;
    }


    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
