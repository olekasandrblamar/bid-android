package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Ticket;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class PurchaseTicketResponse implements Serializable {

    private Ticket ticket;

    @JsonProperty("error")
    private ErrorStatus errorStatus;

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(ErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
