package com.bid.app.model.view;

import androidx.annotation.NonNull;

import com.bid.app.model.view.RoutePath;
import com.bid.app.model.view.Waypoint;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class AvailableRoute implements Serializable {

    @JsonProperty("id")
    String id;

    @JsonProperty("waypoints")
    List<Waypoint> waypoints;

    @JsonProperty("paths")
    List<RoutePath> paths;

    public String getId() {
        return id;
    }

    public List<RoutePath> getPaths() {
        return paths;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setPaths(List<RoutePath> paths) {
        this.paths = paths;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
