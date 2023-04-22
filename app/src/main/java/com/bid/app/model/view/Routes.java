package com.bid.app.model.view;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Routes {

    @SerializedName("routes")
    @Expose
    private ArrayList<ArrayList<String>> routes;

    public ArrayList<ArrayList<String>> getRoute() {
        return routes;
    }

    public void setRoute(ArrayList<ArrayList<String>> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
