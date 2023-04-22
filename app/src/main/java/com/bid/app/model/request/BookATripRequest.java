package com.bid.app.model.request;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Step;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class BookATripRequest implements Serializable {

    @JsonProperty("from")
    private String from;

    @JsonProperty("from_location")
    private String fromLocation;

    @JsonProperty("to")
    private String to;

    @JsonProperty("to_location")
    private String toLocation;

    @JsonProperty("steps")
    private List<Step> steps;


    @JsonProperty("family_member_id")
    private String familyMemberId;


    @JsonProperty("error")
    private ErrorStatus error;



    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public String getFamilyMemberId() {
        return familyMemberId;
    }

    public void setFamilyMemberId(String familyMemberId) {
        this.familyMemberId = familyMemberId;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public ErrorStatus getError() {
        return error;
    }

    public void setError(ErrorStatus error) {
        this.error = error;
    }


    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
