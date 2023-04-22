package com.bid.app.model.view;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class PersonalDetail implements Parcelable {

    private String name;

    private String desc;

    private String hint;

    private int resourceOne;

    private int resourceTwo;

    private boolean isSelected;

    public PersonalDetail() {

    }

    public PersonalDetail(String name, String desc, String hint, int imageLeft, int imageRight, boolean select) {
        this.name = name;
        this.desc = desc;
        this.hint = hint;
        this.resourceOne = imageLeft;
        this.resourceTwo = imageRight;
        this.isSelected = select;
    }

    protected PersonalDetail(Parcel in) {
        name = in.readString();
        desc = in.readString();
        hint = in.readString();
        resourceOne = in.readInt();
        resourceTwo = in.readInt();
        isSelected = in.readBoolean();
    }

    public static final Creator<PersonalDetail> CREATOR = new Creator<PersonalDetail>() {
        @Override
        public PersonalDetail createFromParcel(Parcel in) {
            return new PersonalDetail(in);
        }

        @Override
        public PersonalDetail[] newArray(int size) {
            return new PersonalDetail[size];
        }
    };

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

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public static Creator<PersonalDetail> getCREATOR() {
        return CREATOR;
    }

    public int getResourceOne() {
        return resourceOne;
    }

    public void setResourceOne(int resourceOne) {
        this.resourceOne = resourceOne;
    }

    public int getResourceTwo() {
        return resourceTwo;
    }

    public void setResourceTwo(int resourceTwo) {
        this.resourceTwo = resourceTwo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
        dest.writeString(desc);
        dest.writeString(hint);
        dest.writeInt(resourceOne);
        dest.writeInt(resourceTwo);
        dest.writeBoolean(isSelected);
    }
}
