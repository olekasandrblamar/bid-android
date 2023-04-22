package com.bid.app.model.response;

import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Symptoms;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;

public class DiseaseResponse {

    @JsonProperty("error")
    private ErrorStatus error;

    @JsonProperty("data")
    private ArrayList<Disease> diseaseArrayList;

    public ErrorStatus getError() {
        return error;
    }

    public Disease getDisease(int index) {
        return diseaseArrayList.get(index);
    }

    public void setError(ErrorStatus error) {
        this.error = error;
    }

    public ArrayList<Disease> getDiseaseArrayList() {
        return diseaseArrayList;
    }

    public void setDiseaseArrayList(ArrayList<Disease> disease_list) {
        this.diseaseArrayList = disease_list;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
