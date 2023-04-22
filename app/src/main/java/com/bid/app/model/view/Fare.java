package com.bid.app.model.view;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Fare {

    String id;
    String user_type;
    String price;
    String created_at;
    String updated_at;

    public String getId() {
        return id;
    }

    public String getUser_type() {
        return user_type;
    }


    public String getPrice() {
        return price;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
