package com.bid.app.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;

public class IslandInfo {
    @SerializedName("island_id")
    @Expose
    private String island_id;

    @SerializedName("island_name")
    @Expose
    private String island_name;

    @SerializedName("medical_providers")
    @Expose
    private ArrayList<MedicalProvider> medical_providers;

    public String getIsland_id() {
        return island_id;
    }

    public void setIsland_id(String island_id) {
        this.island_id = island_id;
    }

    public String getIsland_name() {
        return island_name;
    }

    public void setIsland_name(String island_name) {
        this.island_name = island_name;
    }

    public ArrayList<MedicalProvider> getMedical_providers() {
        return medical_providers;
    }

    public void setMedical_providers(ArrayList<MedicalProvider> medical_providers) {
        this.medical_providers = medical_providers;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
