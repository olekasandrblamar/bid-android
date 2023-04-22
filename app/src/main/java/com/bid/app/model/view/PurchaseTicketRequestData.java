package com.bid.app.model.view;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class PurchaseTicketRequestData implements Serializable {


    @JsonProperty("seatNo")
    private String seatNo;

    @JsonProperty("ticketId")
    private String ticketId;

    @JsonProperty("busId")
    private String busId;

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
