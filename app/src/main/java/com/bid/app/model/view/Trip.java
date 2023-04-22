package com.bid.app.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class Trip implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("from")
    private String from;

    @JsonProperty("from_location")
    private String fromLocation;

    @JsonProperty("to")
    private String to;

    @JsonProperty("to_location")
    private String toLocation;

    @JsonProperty("available")
    private String available;


    @JsonProperty("tickets")
    private List<Ticket> tickets;

    @JsonProperty("type")
    private String type;

    public String getType() {
        return type;
    }

    public String getToLocation() {
        return toLocation;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public String getAvailable() {
        return available;
    }



    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
