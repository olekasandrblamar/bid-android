package com.bid.app.model.response;

import com.bid.app.model.view.Country;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CountryListResponse {

    @JsonProperty("countries")
    private List<Country> countryList;

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }
}
