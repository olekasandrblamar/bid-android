package com.bid.app.model.view;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Country implements Parcelable {

    private String name;

    private String dial_code;

    private String code;

    public Country(){

    }

    protected Country(Parcel in) {
        name = in.readString();
        dial_code = in.readString();
        code = in.readString();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDial_code() {
        return dial_code;
    }

    public void setDial_code(String dial_code) {
        this.dial_code = dial_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(dial_code);
        dest.writeString(code);
    }
}
