package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class GetSpecificCardResponse {
    @JsonProperty("error")
    private ErrorStatus errorStatus;

    @JsonProperty("response")
    private CardListInfo response;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }


    public CardListInfo getResponse() {
        return response;
    }


    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
