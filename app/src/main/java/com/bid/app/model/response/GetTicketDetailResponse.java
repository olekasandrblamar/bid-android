package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Ticket;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class GetTicketDetailResponse implements Serializable {

    @JsonProperty("error")
    private ErrorStatus error;


    @JsonProperty("data")
    private Ticket ticket;

    public Ticket getTicket() {
        return ticket;
    }

    public ErrorStatus getError() {
        return error;
    }

    public void setError(ErrorStatus error) {
        this.error = error;
    }


    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
