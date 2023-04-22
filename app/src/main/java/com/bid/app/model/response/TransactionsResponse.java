package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class TransactionsResponse {
    @JsonProperty("error")
    private ErrorStatus errorStatus;

    @JsonProperty("data")
    private List<TransactionsHistory> transactionsInfos;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(ErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
    }

    public List<TransactionsHistory> getTransactionsInfos() {
        return transactionsInfos;
    }

    public void setTransactionsInfos(List<TransactionsHistory> transactionsInfos) {
        this.transactionsInfos = transactionsInfos;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }


}
