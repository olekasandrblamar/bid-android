package com.bid.app.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SlotDetails {

    @SerializedName("free")
    @Expose
    private String free;

    @SerializedName("paid")
    @Expose
    private String paid;

    @SerializedName("holiday")
    @Expose
    private String holiday;

    //@Expose(serialize = false , deserialize = false)
    private transient String time; //Have not to send to server
    //@Expose(serialize = false , deserialize = false)
    private transient Boolean is_expired = false; //Have not to send to server

    public Boolean getIs_expired() {
        return is_expired;
    }

    public void setIs_expired(Boolean is_expired) {
        this.is_expired = is_expired;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }
}
