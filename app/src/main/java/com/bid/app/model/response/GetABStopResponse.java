package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.CurrentPos;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Stop;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class GetABStopResponse implements Serializable {
    @JsonProperty("error")
    private ErrorStatus errorStatus;

    @JsonProperty("passed_stop")
    private Stop passedStop;

    @JsonProperty("toward_stop")
    private Stop towardStop;

    @JsonProperty("ongoing")
    private String ongoing;

    public String getOngoing() {
        return ongoing;
    }

    public Stop getTowardStop() {
        return towardStop;
    }

    public Stop getPassedStop() {
        return passedStop;
    }

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }


    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
