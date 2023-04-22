package com.bid.app.ui.fragment.IdDocuments;

public class DocumentUploadResponseModel {

    private String status;
    private String message;
    private ScannedIDRModel  data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ScannedIDRModel getData() {
        return data;
    }

    public void setData(ScannedIDRModel data) {
        this.data = data;
    }
}
