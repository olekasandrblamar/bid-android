package com.bid.app.model.request;

import com.bid.app.ui.fragment.IdDocuments.ScannedIDRModel;

public class BIDRequestModel {

    private String first_name;
    private String last_name;
    private String user_id;
    private ScannedIDRModel user_data;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public ScannedIDRModel getUser_data() {
        return user_data;
    }

    public void setUser_data(ScannedIDRModel user_data) {
        this.user_data = user_data;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
