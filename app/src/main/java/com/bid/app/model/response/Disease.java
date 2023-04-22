package com.bid.app.model.response;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Disease {

    private String id;
    private String name;
    private String level;

    public String getId() { return id;}
    public String getName() { return name;}
    public String getLevel() { return level;}

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
