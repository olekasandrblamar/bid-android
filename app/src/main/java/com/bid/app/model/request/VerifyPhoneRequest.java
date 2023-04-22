package com.bid.app.model.request;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class VerifyPhoneRequest {

    private String mobile;

    private String code;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
