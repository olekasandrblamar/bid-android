package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.Attachment;
import com.bid.app.model.request.AddressRequestEdit;
import com.bid.app.model.view.ErrorStatus;
import com.bid.app.model.view.Fare;
import com.bid.app.model.view.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

@JsonPropertyOrder({"error", "role"})
public class LoginResponse implements Serializable {

    @JsonProperty("error")
    private ErrorStatus error;

    @JsonProperty("id")
    private String id;

    @JsonProperty("b_id")
    private String b_id;

    @JsonProperty("role_id")
    private String roleId;

    @JsonProperty("username")
    private String userName;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("rating")
    private String rating;

    @JsonProperty("rating_cnt")
    private String ratingCnt;

    private String votes;

    private String rank;

    @JsonProperty("total_votes")
    private String totalVotes;

    @JsonProperty("subscription_end_date")
    private String userSubscription;

    private String mobile;

    private String dob;

    @JsonProperty("role")
    private UserRole userRole;

    private Attachment attachment;

    @JsonProperty("address")
    private AddressRequestEdit addressRequestEdit;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("employee_status")
    private String employee_status;

    @JsonProperty("description")
    private String description;

    @JsonProperty("is_bus")
    private String isBus;

    @JsonProperty("bus_id")
    private String busId;

    @JsonProperty("fare")
    private Fare fare;

    public Fare getFare() {
        return fare;
    }

    public String getRating() {
        return rating;
    }

    public String getRatingCnt() {
        return ratingCnt;
    }

    public String getIsBus() {
        return isBus;
    }

    public void setIsBus(String isBus) {
        this.isBus = isBus;
    }

    public String getB_id() {
        return b_id;
    }

    public void setB_id(String b_id) {
        this.b_id = b_id;
    }

    public ErrorStatus getError() {
        return error;
    }

    public void setError(ErrorStatus error) {
        this.error = error;
    }

  /*  public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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

    public String getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(String totalVotes) {
        this.totalVotes = totalVotes;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getUserSubscription() {
        return userSubscription;
    }

    public void setUserSubscription(String userSubscription) {
        this.userSubscription = userSubscription;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public AddressRequestEdit getAddressRequestEdit() {
        return addressRequestEdit;
    }

    public void setAddressRequestEdit(AddressRequestEdit addressRequestEdit) {
        this.addressRequestEdit = addressRequestEdit;
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

    public String getBusId() {
        return busId;
    }


    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
