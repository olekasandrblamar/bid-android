package com.bid.app.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdDocumentsData {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("created_at")
    private String created_at;
    @JsonProperty("updated_at")
    private String updated_at;
    @JsonProperty("name")
    private String name;
    @JsonProperty("is_required")
    private Integer is_required;
    @JsonProperty("is_active")
    private Integer is_active;
    @JsonProperty("document")
    private Document document;

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIs_required() {
        return is_required;
    }

    public void setIs_required(Integer is_required) {
        this.is_required = is_required;
    }

    public Integer getIs_active() {
        return is_active;
    }

    public void setIs_active(Integer is_active) {
        this.is_active = is_active;
    }
}
