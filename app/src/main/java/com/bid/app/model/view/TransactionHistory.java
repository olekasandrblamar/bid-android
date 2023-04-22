package com.bid.app.model.view;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class TransactionHistory {

    private String date;

    private String hospital;

    private String test;

    private String card;

    private String amount;

    public TransactionHistory() {

    }

    public TransactionHistory(String date, String hospital, String test, String card, String amount) {
        this.date = date;
        this.hospital = hospital;
        this.test = test;
        this.card = card;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
