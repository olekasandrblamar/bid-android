package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;

public class CenterListResponse {

    @SerializedName("error")
    @Expose
    private ErrorStatus error;

    @SerializedName("data")
    @Expose
    private ArrayList<IslandInfo> islands;

    public ErrorStatus getError() {
        return error;
    }

    public void setError(ErrorStatus error) {
        this.error = error;
    }

    public ArrayList<IslandInfo> getIslands() {
        return islands;
    }

    public void setIslands(ArrayList<IslandInfo> islands) {
        this.islands = islands;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
