package com.bid.app.model.view;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class CheckUpTestCase {

    private int number;

    private String name;

    private String desc;

    public CheckUpTestCase(int i, String s, String s1) {
        this.number = i;
        this.name = s;
        this.desc = s1;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
