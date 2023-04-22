package com.bid.app.model.view;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class AddIDDocumentRequestDataModel implements Serializable {

    private String surnames;
    private String givenNames;
    private String countryCode;
    private String documentNumber;
    private String nationality;
    private String birthDate;
    private String sex;

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getGivenNames() {
        return givenNames;
    }

    public String getNationality() {
        return nationality;
    }

    public String getSex() {
        return sex;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public void setGivenNames(String givenNames) {
        this.givenNames = givenNames;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
