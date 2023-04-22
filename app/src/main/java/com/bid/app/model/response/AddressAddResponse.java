package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.Address;
import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

@JsonPropertyOrder({"error", "data"})
public class AddressAddResponse implements Serializable {

    @JsonProperty("error")
    private ErrorStatus errorStatus;

    @JsonProperty("data")
    private Address address;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(ErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
