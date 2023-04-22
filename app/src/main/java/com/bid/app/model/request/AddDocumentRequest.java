package com.bid.app.model.request;

import androidx.annotation.NonNull;

import com.bid.app.model.view.AddIDDocumentRequestDataModel;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Step;
import com.bid.app.ui.fragment.IdDocuments.ScannedIDRModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class AddDocumentRequest implements Serializable {

    @JsonProperty("image")
    private String image;

    @JsonProperty("document_id")
    private String documentId;

    @JsonProperty("user_data")
    private ScannedIDRModel userData;

    @JsonProperty("location")
    private String location;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("data")
    private AddIDDocumentRequestDataModel data;

    public void setUserData(ScannedIDRModel userData) {
        this.userData = userData;
    }

    public void setData(AddIDDocumentRequestDataModel data) {
        this.data = data;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }



    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
