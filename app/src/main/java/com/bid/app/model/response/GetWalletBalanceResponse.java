package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class GetWalletBalanceResponse {
    @JsonProperty("error")
    private ErrorStatus errorStatus;

    @JsonProperty("balance")
    private String balance;

    @JsonProperty("total_trips")
    private String totalTrips;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }


    public String getBalance() {
        return balance;
    }

    public String getTotalTrips() {
        return totalTrips;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
