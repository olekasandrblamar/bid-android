package com.bid.app.model.response;

import com.bid.app.model.view.Symptoms;

import java.io.Serializable;
import java.util.List;

public class CheckupTest implements Serializable {

    private String result;

    private List<String> symptomsIds;

    private String covidContact;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getSymptomsIds() {
        return symptomsIds;
    }

    public void setSymptomsIds(List<String> symptomsIds) {
        this.symptomsIds = symptomsIds;
    }

    public String getCovidContact() {
        return covidContact;
    }

    public void setCovidContact(String covidContact) {
        this.covidContact = covidContact;
    }
}
