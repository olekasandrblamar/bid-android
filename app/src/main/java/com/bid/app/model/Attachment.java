package com.bid.app.model;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class Attachment implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("class")
    private String className;

    @JsonProperty("foreign_id")
    private String foreignId;

    @JsonProperty("filename")
    private String filename;

    @JsonProperty("mimetype")
    private String mimetype;

    @JsonProperty("height")
    private String height;

    @JsonProperty("width")
    private String width;

    @JsonProperty("dir")
    private String dir;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }


    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
