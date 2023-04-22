package com.bid.app.model.request;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

@JsonPropertyOrder({"card_number", "name", "cvv", "expiry_date"})
public class TopUpByCardRequest implements Serializable {

    private String cvv;

    private String amount;

    @JsonProperty("card_id")
    private String cardId;

    public String getCvv() {
        return cvv;
    }

    public String getAmount() {
        return amount;
    }
    public String getCardId() {
        return cardId;
    }
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
