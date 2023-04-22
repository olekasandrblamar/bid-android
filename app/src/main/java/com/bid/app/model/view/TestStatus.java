package com.bid.app.model.view;

import com.google.gson.annotations.SerializedName;

public class TestStatus {

    @SerializedName("id")
    private String id ;

    @SerializedName("name")
    private String name;

    @SerializedName("color")
    private String color;

    @SerializedName("font_color")
    private String font_color;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFont_color() {
        return font_color;
    }

    public void setFont_color(String font_color) {
        this.font_color = font_color;
    }
}
