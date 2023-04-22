package com.bid.app.model.response;

import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.StaticPage;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class StaticPageResponse {

    @JsonProperty("error")
    private ErrorStatus error;

    @JsonProperty("data")
    private StaticPage staticPage;

    public ErrorStatus getError() {
        return error;
    }

    public void setError(ErrorStatus error) {
        this.error = error;
    }

    public StaticPage getStaticPage() {
        return staticPage;
    }

    public void setStaticPage(StaticPage staticPage) {
        this.staticPage = staticPage;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
