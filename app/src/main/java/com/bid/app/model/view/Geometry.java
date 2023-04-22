package com.bid.app.model.view;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Geometry {

    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
