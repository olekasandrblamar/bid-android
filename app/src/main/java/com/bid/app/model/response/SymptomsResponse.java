package com.bid.app.model.response;

import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Symptoms;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;

public class SymptomsResponse {

    @JsonProperty("error")
    private ErrorStatus error;

    @JsonProperty("data")
    private ArrayList<Symptoms> symptomsList;

    public ErrorStatus getError() {
        return error;
    }

    public void setError(ErrorStatus error) {
        this.error = error;
    }

    public ArrayList<Symptoms> getSymptomsList() {
        return symptomsList;
    }

    public void setSymptomsList(ArrayList<Symptoms> symptomsList) {
        this.symptomsList = symptomsList;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
