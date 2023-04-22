package com.bid.app.model.view;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class RoutePath implements Serializable {
    @JsonProperty("from")
    String from;

    @JsonProperty("to")
    String to;

    @JsonProperty("path")
    List<String> path;

    public String getFrom() {
        return from;
    }

    public List<String> getPath() {
        return path;
    }

    public String getTo() {
        return to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
