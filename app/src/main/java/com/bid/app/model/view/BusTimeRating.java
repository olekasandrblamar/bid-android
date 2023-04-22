package com.bid.app.model.view;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class BusTimeRating {

    private String eta;

    private String rating;

    public String getEta() {
        return eta;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
