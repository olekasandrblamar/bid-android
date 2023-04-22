package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.Attachment;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class NotificationListInfo {
    private String id;
    private String created_at;
    private String updated_at;
    private String type_id;
    private String type;
    private String foreign_id;
    private String from_user_id;
    private String user_id;
    private String title;
    private String body;
    private String is_read;
    private Attachment attachment;

    public String getType() {
        return type;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public String getForeign_id() {
        return foreign_id;
    }

    public String getFrom_user_id() {
        return from_user_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setForeign_id(String foreign_id) {
        this.foreign_id = foreign_id;
    }

    public void setFrom_user_id(String from_user_id) {
        this.from_user_id = from_user_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
