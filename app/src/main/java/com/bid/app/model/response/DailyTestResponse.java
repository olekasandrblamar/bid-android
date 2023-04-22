package com.bid.app.model.response;

import com.bid.app.model.view.DailyTest;
import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DailyTestResponse {



    @JsonProperty("error")
    private ErrorStatus status;

    public ErrorStatus getStatus() {
        return status;
    }

    public void setStatus(ErrorStatus status) {
        this.status = status;
    }
}
