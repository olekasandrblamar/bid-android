package com.bid.app.model.view;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class NotificationSettings {

    private String name;

    private String desc;

    private boolean isSelected;

    public NotificationSettings() {

    }

    public NotificationSettings(String name, String desc, boolean isSelected) {
        this.name = name;
        this.desc = desc;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
