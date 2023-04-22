package com.bid.app.enums;

public enum ERESTServiceStatus {

    REST_OK_CODE("0"), REST_KO_CODE("1"), REST_EXCEPTION("REST_EXCEPTION");

    private String restApiStatus = null;

    private ERESTServiceStatus(String status) {
        this.restApiStatus = status;
    }

    public String getRestApiStatus() {
        return restApiStatus;
    }

    public void setRestApiStatus(String restApiStatus) {
        this.restApiStatus = restApiStatus;
    }

    @Override
    public String toString() {
        return restApiStatus;
    }
}
