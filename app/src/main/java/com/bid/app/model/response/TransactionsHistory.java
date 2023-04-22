package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.PaymentGateway;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class TransactionsHistory {

    private String created_at;
    private String user_id;
    private String to_user_id;
    private String parent_user_id;
    private String transaction_type;
    private String foreign_id;
    private String payment_gateway_id;
    private String amount;
    private String card_id;
    private User other_user;
    private String ticket_id;



    @JsonProperty("user")
    private User userList;


    @JsonProperty("payment_gateway")
    private PaymentGateway payment_gateway;


    public String getTicket_id() {
        return ticket_id;
    }

    public String getTransaction_type() {
        return transaction_type;
    }


    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getParent_user_id() {
        return parent_user_id;
    }

    public void setParent_user_id(String parent_user_id) {
        this.parent_user_id = parent_user_id;
    }

    public String getForeign_id() {
        return foreign_id;
    }

    public void setForeign_id(String foreign_id) {
        this.foreign_id = foreign_id;
    }

    public String getPayment_gateway_id() {
        return payment_gateway_id;
    }

    public void setPayment_gateway_id(String payment_gateway_id) {
        this.payment_gateway_id = payment_gateway_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public User getOther_user() {
        return other_user;
    }

    public void setOther_user(User other_user) {
        this.other_user = other_user;
    }


    public User getUserList() {
        return userList;
    }

    public void setUserList(User userList) {
        this.userList = userList;
    }

    public PaymentGateway getPayment_gateway() {
        return payment_gateway;
    }

    public void setPayment_gateway(PaymentGateway payment_gateway) {
        this.payment_gateway = payment_gateway;
    }
    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
