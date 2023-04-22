package com.bid.app.model.view;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class Restaurant {

    private String vicinity;

    private String name;

    private Geometry geometry;

    private List<Photo> photos;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}

