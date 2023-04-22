package com.bid.app.model.view;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class DailyTestRequest {

    private String covid_contact;

    private String result;

    private List<String> symptoms;

    public String getCovid_contact() {
        return covid_contact;
    }

    public void setCovid_contact(String covid_contact) {
        this.covid_contact = covid_contact;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<String> symptoms) {
        this.symptoms = symptoms;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
