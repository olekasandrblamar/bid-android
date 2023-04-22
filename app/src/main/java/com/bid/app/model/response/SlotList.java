
package com.bid.app.model.response;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class SlotList {
    private String count;

    private String time;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
