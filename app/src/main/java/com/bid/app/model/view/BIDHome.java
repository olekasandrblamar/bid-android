package com.bid.app.model.view;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class BIDHome {

    private String name;

    private int resource;

    public BIDHome() {

    }

    public BIDHome(String name, int image) {
        this.name = name;
        this.resource = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
