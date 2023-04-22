package com.bid.app.model.response;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class CardListInfo implements Serializable {
    private String id;
    private String name;
    private String user_id;
    private String card_number;
    private String card_display_number;
    private String expiry_date;
    private String card_type;

    private CardUserInfo cardUserInfo;

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCard_display_number() {
        return card_display_number;
    }

    public void setCard_display_number(String card_display_number) {
        this.card_display_number = card_display_number;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public CardUserInfo getCardUserInfo() {
        return cardUserInfo;
    }

    public void setCardUserInfo(CardUserInfo cardUserInfo) {
        this.cardUserInfo = cardUserInfo;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
