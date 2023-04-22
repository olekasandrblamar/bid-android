package com.bid.app.model.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class DailyTest {

    private String id;

    private String created_at;

    private String updated_at;

    private String user_id;

    private String fill_date;

    private String covid_contact;

    private String result;

    @JsonProperty("symptoms")
    private List<DailyTestSymptoms> symptoms;


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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFill_date() {
        return fill_date;
    }

    public void setFill_date(String fill_date) {
        this.fill_date = fill_date;
    }

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

    public List<DailyTestSymptoms> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<DailyTestSymptoms> symptoms) {
        this.symptoms = symptoms;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
