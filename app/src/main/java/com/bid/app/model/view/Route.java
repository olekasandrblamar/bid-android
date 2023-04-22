package com.bid.app.model.view;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private List<String> route;

    public List<String> getRoute() {
        return route;
    }
    public void setRoutes(List<String> route) {
        this.route = route;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
