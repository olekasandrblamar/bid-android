package com.bid.app.model.response;

import java.io.Serializable;
import java.util.ArrayList;

public class Questionnaire implements Serializable {
    private ContactOfKnownCase contactOfKnownCase;
    private String reasonForTest;
    private String nameOfReferral;
    private ArrayList<Integer> symptoms;
    private Boolean contact;
    private String disease;
    public ContactOfKnownCase getContactOfKnownCase() {
        return contactOfKnownCase;
    }

    public void setContactOfKnownCase(ContactOfKnownCase contactOfKnownCase) {
        this.contactOfKnownCase = contactOfKnownCase;
    }

    public String getReasonForTest() {
        return reasonForTest;
    }

    public void setReasonForTest(String reasonForTest) {
        this.reasonForTest = reasonForTest;
    }

    public String getNameOfReferral() {
        return nameOfReferral;
    }

    public void setNameOfReferral(String nameOfReferral) {
        this.nameOfReferral = nameOfReferral;
    }

    public ArrayList<Integer> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<Integer> symptoms) {
        this.symptoms = symptoms;
    }

    public Boolean getContact() {
        return contact;
    }

    public void setContact(Boolean contact) {
        this.contact = contact;
    }

    // disease
    public void setDisease(String disease_type) {
        this.disease = disease_type;
    }

    public String getDisease() {
        return disease;
    }
}
