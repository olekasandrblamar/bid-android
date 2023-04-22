package com.bid.app.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class DailyTestSymptoms {

    private String id;

    private String created_at;

    private String updated_at;

    private String daily_test_id;

    private String symptom_id;

    @JsonProperty("symptom")
    private Symptoms symptoms;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDaily_test_id() {
        return daily_test_id;
    }

    public void setDaily_test_id(String daily_test_id) {
        this.daily_test_id = daily_test_id;
    }

    public String getSymptom_id() {
        return symptom_id;
    }

    public void setSymptom_id(String symptom_id) {
        this.symptom_id = symptom_id;
    }

    public Symptoms getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(Symptoms symptoms) {
        this.symptoms = symptoms;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
