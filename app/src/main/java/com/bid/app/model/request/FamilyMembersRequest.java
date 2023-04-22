package com.bid.app.model.request;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.bid.app.ui.fragment.IdDocuments.ScannedIDRModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class FamilyMembersRequest implements Serializable {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;


    @JsonProperty("relationship")
    private String relationShip;

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("user_data")
    private ScannedIDRModel userData;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setRelationShip(String relationShip) {
        this.relationShip = relationShip;
    }

    public String getRelationShip() {
        return relationShip;
    }

    public ScannedIDRModel getUserData() {
        return userData;
    }

    public void setUserData(ScannedIDRModel userData) {
        this.userData = userData;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
