package com.bid.app.model.response;

import com.bid.app.model.view.LocationInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;

public class MedicalProvider {
    @SerializedName("medical_provider_id")
    @Expose
    private String medical_provider_id;

    @SerializedName("medical_provider")
    @Expose
    private String medical_provider;

    @SerializedName("locations")
    @Expose
    private ArrayList<LocationInfo> locations;

    public String getMedical_provider_id() {
        return medical_provider_id;
    }

    public void setMedical_provider_id(String medical_provider_id) {
        this.medical_provider_id = medical_provider_id;
    }

    public String getMedical_provider() {
        return medical_provider;
    }

    public void setMedical_provider(String medical_provider) {
        this.medical_provider = medical_provider;
    }

    public ArrayList<LocationInfo> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<LocationInfo> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
