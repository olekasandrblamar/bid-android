package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class CardsListResponse {
    @JsonProperty("error")
    private ErrorStatus errorStatus;

    @JsonProperty("data")
    private List<CardListInfo> cardListInfo;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(ErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
    }

    public List<CardListInfo> getCardListInfo() {
        return cardListInfo;
    }

    public void setCardListInfo(List<CardListInfo> cardListInfo) {
        this.cardListInfo = cardListInfo;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
