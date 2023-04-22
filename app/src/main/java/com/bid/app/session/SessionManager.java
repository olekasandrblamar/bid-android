package com.bid.app.session;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.bid.app.R;
import com.bid.app.interfaces.IPreferenceConstants;
import com.bid.app.model.view.Fare;

public class SessionManager {

    private Context mContext;
    private SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        this.mContext = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE);
    }

    public void setUserId(final String userId) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_USER_ID, userId);
        edit.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_USER_ID, "");
    }


    public void setBid(final String userId) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_B_ID, userId);
        edit.apply();
    }

    public String getBid() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_B_ID, "");
    }

    public void setIsBus(final String userId) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_IS_BUS, userId);
        edit.apply();
    }


    public String getIsBus() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_IS_BUS, "");
    }
    public void setBusId(final String busId) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_BUS_ID, busId);
        edit.apply();
    }
    public String getBusId() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_BUS_ID, "0");
    }
    public void setFare(final Fare fare) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_FARE, fare.getPrice());
        edit.apply();
    }
    public Fare getFare() {
        Fare fare = new Fare();
        fare.setPrice(sharedPreferences.getString(IPreferenceConstants.PREF_FARE, ""));
        return fare;
    }

    public void setUserRole(final String roleId) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_USER_ROLE, roleId);
        edit.apply();
    }

    public String getUserRole() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_USER_ROLE, "");
    }

    public void setUserImage(final String roleId) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_USER_IMAGE, roleId);
        edit.apply();
    }

    public String getUserImage() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_USER_IMAGE, "");
    }

    public void setMobileNumber(final String mobileNumber) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_MOBILE_NUMBER, mobileNumber);
        edit.apply();
    }

    public String getMobileNumber() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_MOBILE_NUMBER, "");
    }

    public void setFirstName(final String firstName) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_FIRST_NAME, firstName);
        edit.apply();
    }

    public String getFirstName() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_FIRST_NAME, "");
    }

    public void setLastName(final String lastName) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_LAST_NAME, lastName);
        edit.apply();
    }

    public String getLastName() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_LAST_NAME, "");
    }

    public void setUsername(final String userName) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_USER_NAME, userName);
        edit.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_USER_NAME, "");
    }

    public void setAccessToken(final String accessToken) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_ACCESS_TOKEN, accessToken);
        edit.apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_ACCESS_TOKEN, "");
    }

    public void setRefreshToken(final String refreshToken) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_REFRESH_TOKEN, refreshToken);
        edit.apply();
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_REFRESH_TOKEN, "");
    }

    public void setPassword(final String password) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_PASSWORD, password);
        edit.apply();
    }

    public String getPassword() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_PASSWORD, "");
    }

    public void setFCMToken(final String id) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_FCM_TOKEN, id);
        edit.apply();
    }

    public String getFCMToken() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_FCM_TOKEN, "");
    }

    public void setAddressOne(final String id) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_ADDRESS_ONE, id);
        edit.apply();
    }

    public String getAddressOne() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_ADDRESS_ONE, "");
    }

    public void setAddressTwo(final String id) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_ADDRESS_TWO, id);
        edit.apply();
    }

    public String getAddressTwo() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_ADDRESS_TWO, "");
    }

    public void setCity(final String id) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_CITY, id);
        edit.apply();
    }

    public String getCity() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_CITY, "");
    }

    public void setState(final String id) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_STATE, id);
        edit.apply();
    }

    public String getState() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_STATE, "");
    }

    public void setCountry(final String id) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_COUNTRY, id);
        edit.apply();
    }

    public String getCountry() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_COUNTRY, "");
    }

    public void setGender(final String id) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_GENDER, id);
        edit.apply();
    }

    public String getGender() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_GENDER, "");
    }

    public void setNationality(final String id) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_NATIONALITY, id);
        edit.apply();
    }

    public String getNationality() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_NATIONALITY, "");
    }

    public void setZipCode(final String id) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_ZIPCODE, id);
        edit.apply();
    }

    public String getZipCode() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_ZIPCODE, "");
    }

    public void setDob(final String dob) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_DOB, dob);
        edit.apply();
    }

    public String getDob() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_DOB, "");
    }

    public void setEmail(final String mobileNumber) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_EMAIL, mobileNumber);
        edit.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_EMAIL, "");
    }

    public void clearSession() {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.apply();
    }

    public String getDescription() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_DESCRIPTION, "");
    }

    public String getEmploymentStatus() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_EMPLOYMENT_STATUS, "");
    }

    public void setDescription(String description) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_DESCRIPTION, description);
        edit.apply();
    }

    public void setEmploymentStatus(String employee_status) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_EMPLOYMENT_STATUS, employee_status);
        edit.apply();
    }

    public void setDriverTapTime(String driverTapTime) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_DRIVER_TAP_TIME, driverTapTime);
        edit.apply();
    }

    public String getDriverTapTime(){
        return sharedPreferences.getString(IPreferenceConstants.PREF_DRIVER_TAP_TIME, "");
    }

    public void setDriverOnline(String driverOnline) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(IPreferenceConstants.PREF_DRIVER_ONLINE, driverOnline);
        edit.apply();
    }

    public String getDriverOnline() {
        return sharedPreferences.getString(IPreferenceConstants.PREF_DRIVER_ONLINE, "");
    }

}
