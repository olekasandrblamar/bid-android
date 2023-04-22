package com.bid.app.ui.fragment.IdDocuments;

import com.bid.app.model.response.ExtractedID;

import java.util.ArrayList;

public class ScannedIDRModel {

    private String namef;
    private String namel;
    private ArrayList<Double> face_data;
    private ExtractedID extIdAdded;

    public String getNamef() {
        return namef;
    }

    public void setNamef(String namef) {
        this.namef = namef;
    }

    public String getNamel() {
        return namel;
    }

    public void setNamel(String namel) {
        this.namel = namel;
    }

    public ArrayList<Double> getFace_data() {
        return face_data;
    }

    public void setFace_data(ArrayList<Double> face_data) {
        this.face_data = face_data;
    }

    public ExtractedID getExtIdAdded() {
        return extIdAdded;
    }

    public void setExtIdAdded(ExtractedID extIdAdded) {
        this.extIdAdded = extIdAdded;
    }
}
