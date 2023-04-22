package com.bid.app.model.view;

import androidx.annotation.NonNull;

import com.bid.app.model.Attachment;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public class Profile implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("b_id")
    private String b_id;

    @JsonProperty("username")
    private String userName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("votes")
    private String votes;

    @JsonProperty("rank")
    private String rank;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("country")
    private String country;

    @JsonProperty("zipcode")
    private String zipCode;

    @JsonProperty("subscription_end_date")
    private String subsEndDate;

    @JsonProperty("is_subscribed")
    private boolean isSubscribed;

    @JsonProperty("is_admin_approval")
    private boolean isAdminApproval;

    @JsonProperty("is_paypal_connect")
    private boolean isPayPalConnect;

    @JsonProperty("attachment")
    private Attachment attachment;

    private Address address;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("employee_status")
    private String employee_status;

    @JsonProperty("description")
    private String description;

    @JsonProperty("dob")
    private String dob;

    public String getB_id() {
        return b_id;
    }

    public void setB_id(String b_id) {
        this.b_id = b_id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmployee_status() {
        return employee_status;
    }

    public void setEmployee_status(String employee_status) {
        this.employee_status = employee_status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getSubsEndDate() {
        return subsEndDate;
    }

    public void setSubsEndDate(String subsEndDate) {
        this.subsEndDate = subsEndDate;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean getAdminApproval() {
        return isAdminApproval;
    }

    public void setAdminApproval(boolean adminApproval) {
        isAdminApproval = adminApproval;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public boolean isAdminApproval() {
        return isAdminApproval;
    }


    public boolean isPayPalConnect() {
        return isPayPalConnect;
    }

    public void setPayPalConnect(boolean payPalConnect) {
        isPayPalConnect = payPalConnect;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
