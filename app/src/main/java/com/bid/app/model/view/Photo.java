package com.bid.app.model.view;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Photo {

    private String height;

    private String photo_reference;

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
