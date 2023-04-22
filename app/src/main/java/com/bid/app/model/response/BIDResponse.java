package com.bid.app.model.response;

import com.bid.app.model.view.ErrorStatus;

public class BIDResponse {

    private String b_id;
    private ErrorStatus error;

    public String getB_id() {
        return b_id;
    }

    public void setB_id(String b_id) {
        this.b_id = b_id;
    }

    public ErrorStatus getError() {
        return error;
    }

    public void setError(ErrorStatus error) {
        this.error = error;
    }
}
