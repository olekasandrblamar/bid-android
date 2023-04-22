package com.bid.app.model.view;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class StaticPage {

    private int id;
    private String title;
    private String content;
    private String url;
    private String dispaly_url;
    private String slug;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDispaly_url() {
        return dispaly_url;
    }

    public void setDispaly_url(String dispaly_url) {
        this.dispaly_url = dispaly_url;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
