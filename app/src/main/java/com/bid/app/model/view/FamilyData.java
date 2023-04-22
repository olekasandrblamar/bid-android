package com.bid.app.model.view;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class FamilyData implements Serializable {

    @JsonProperty("id")
    String id;

    @JsonProperty("user_id")
    String userId;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("lastName")
    String lastName;

    @JsonProperty("name")
    String fullName;

    @JsonProperty("national_id")
    String nationalId;

    @JsonProperty("relationship")
    String relationship;

    @JsonProperty("document")
    String document;

    @JsonProperty("image")
    String image;

    @JsonProperty("mobile")
    String mobile;


    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getDocument() {
        return document;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getImage() {
        return image;
    }

    public String getMobile() {
        return mobile;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getRelationship() {
        return relationship;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
