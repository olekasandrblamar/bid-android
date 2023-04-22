package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.PaymentGateway;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

@JsonPropertyOrder({"error", "data"})
public class PaymentGatewayResponse implements Serializable {

    @JsonProperty("error")
    private ErrorStatus errorStatus;

    @JsonProperty("data")
    private List<PaymentGateway> paymentGatewayList;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(ErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
    }

    public List<PaymentGateway> getPaymentGatewayList() {
        return paymentGatewayList;
    }

    public void setPaymentGatewayList(List<PaymentGateway> paymentGatewayList) {
        this.paymentGatewayList = paymentGatewayList;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
