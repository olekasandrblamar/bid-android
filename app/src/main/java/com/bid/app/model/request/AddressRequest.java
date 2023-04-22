package com.bid.app.model.request;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

@JsonPropertyOrder({"name", "addressline1", "addressline2", "city", "state", "country", "zipCode", "isDefault"})
public class AddressRequest implements Serializable {

    private String name;

    @JsonProperty("addressline1")
    private String addressLine1;

    @JsonProperty("addressline2")
    private String addressLine2;

    private String city;

    private String state;

    private String country;

    @JsonProperty("zipcode")
    private String zipCode;

    @JsonProperty("is_default")
    private String isDefault;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
