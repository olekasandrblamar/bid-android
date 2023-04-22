package com.bid.app.model.view;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Slot {

    private String time;

    private String count;

    public Slot() {

    }

    public Slot(String time, String count) {
        this.time = time;
        this.count = count;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSlot() {
        return count;
    }

    public void setSlot(String slot) {
        this.count = slot;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
