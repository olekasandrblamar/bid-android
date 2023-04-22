package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.GetReviewData;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class GetReviewListResponse implements Serializable {

    @JsonProperty("data")
    private List<GetReviewData> data;


    @JsonProperty("error")
    private ErrorStatus error;

    public void setData(List<GetReviewData> data) {
        this.data = data;
    }

    public List<GetReviewData> getData() {
        return data;
    }

    public ErrorStatus getError() {
        return error;
    }

    public void setError(ErrorStatus error) {
        this.error = error;
    }


    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
