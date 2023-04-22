package com.bid.app.model.request;

import androidx.annotation.NonNull;

import com.bid.app.model.view.PurchaseTicketRequestData;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class PurchaseTicketRequest implements Serializable {


    @JsonProperty("tickets")
    private List<PurchaseTicketRequestData> tickets;

    public void setTickets(List<PurchaseTicketRequestData> tickets) {
        this.tickets = tickets;
    }



    public List<PurchaseTicketRequestData> getTickets() {
        return tickets;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
