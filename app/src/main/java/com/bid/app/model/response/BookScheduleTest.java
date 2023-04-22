package com.bid.app.model.response;

import java.io.Serializable;

public class BookScheduleTest implements Serializable {
    private Questionnaire questionnaire;
    private String reg_date;
    private String cvv;
    private String from_timeslot;
    private String test_type_id;
    private String center_id;
    private String user_id;
    private String card_id;
    private String island_id;
    private String medical_provider_id;
    private String location_id;
    private String payment_type;
    private CardListInfo card_detail;

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getMedical_provider_id() {
        return medical_provider_id;
    }

    public void setMedical_provider_id(String medical_provider_id) {
        this.medical_provider_id = medical_provider_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public CardListInfo getCard_detail() {
        return card_detail;
    }

    public void setCard_detail(CardListInfo card_detail) {
        this.card_detail = card_detail;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIsland_id() {
        return island_id;
    }

    public void setIsland_id(String island_id) {
        this.island_id = island_id;
    }

    public String getCenter_id() {
        return center_id;
    }

    public void setCenter_id(String center_id) {
        this.center_id = center_id;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getTest_type_id() {
        return test_type_id;
    }

    public void setTest_type_id(String test_type_id) {
        this.test_type_id = test_type_id;
    }

    public String getFrom_timeslot() {
        return from_timeslot;
    }

    public void setFrom_timeslot(String from_timeslot) {
        this.from_timeslot = from_timeslot;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }
}
