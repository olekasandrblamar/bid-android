package com.bid.app.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DirectionResults {
    @SerializedName("routes")
    private List<DirectionRoute> routes;

    public List<DirectionRoute> getRoutes() {
        return routes;
    }
}

