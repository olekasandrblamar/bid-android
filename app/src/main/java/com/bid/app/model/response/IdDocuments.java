package com.bid.app.model.response;

import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class IdDocuments {

    @JsonProperty("data")
    private ArrayList<IdDocumentsData> data;
    @JsonProperty("error")
    private ErrorStatus error;

    public ArrayList<IdDocumentsData> getData() {
        return data;
    }

    public void setData(ArrayList<IdDocumentsData> data) {
        this.data = data;
    }

    public ErrorStatus getError() {
        return error;
    }

    public void setError(ErrorStatus error) {
        this.error = error;
    }
}
