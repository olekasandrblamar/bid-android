package com.bid.app.util;

import com.bid.app.model.response.LoginResponse;
import com.bid.app.model.view.Profile;
import com.bid.app.session.SessionManager;

public class SessionManagerUtils {

    public static final void setUserDetailsInSession(final SessionManager sessionManager, final LoginResponse loginResponse, final String password) {

        sessionManager.setUserId(loginResponse.getId());
        sessionManager.setBid(loginResponse.getB_id());
        sessionManager.setUsername(loginResponse.getUserName());
        sessionManager.setFirstName(loginResponse.getFirstName());
        sessionManager.setLastName(loginResponse.getLastName());
        sessionManager.setAccessToken(loginResponse.getAccessToken());
        sessionManager.setRefreshToken(loginResponse.getRefreshToken());
        sessionManager.setPassword(password);
        sessionManager.setMobileNumber(loginResponse.getMobile());
        sessionManager.setDob(loginResponse.getDob());
        sessionManager.setEmail(loginResponse.getEmail());
        sessionManager.setGender(loginResponse.getGender());
        sessionManager.setDescription(loginResponse.getDescription());
        sessionManager.setEmploymentStatus(loginResponse.getEmployee_status());
        sessionManager.setIsBus(loginResponse.getIsBus());
        sessionManager.setFare(loginResponse.getFare());
        sessionManager.setBusId(loginResponse.getBusId());

        String imageUrl = "";
        if (loginResponse.getAttachment() != null) {
            final String hashKey = Utils.getMD5Hash(loginResponse.getAttachment());
//            imageUrl = Constants.API_IMAGE_URL(loginResponse.getAttachment(), hashKey, "jpg");
            imageUrl = Constants.AWS_IMAGE_URL(loginResponse.getAttachment(), "jpg");
        } else {
            imageUrl = Constants.NO_IMAGE_URL;
        }

        sessionManager.setUserImage(imageUrl);

        if (loginResponse.getAddressRequestEdit() != null) {
            sessionManager.setAddressOne(loginResponse.getAddressRequestEdit().getAddressLine1());
            sessionManager.setAddressTwo(loginResponse.getAddressRequestEdit().getAddressLine2());
            sessionManager.setCity(loginResponse.getAddressRequestEdit().getCity());
            sessionManager.setState(loginResponse.getAddressRequestEdit().getState());
            sessionManager.setCountry(loginResponse.getAddressRequestEdit().getCountry());
            sessionManager.setZipCode(loginResponse.getAddressRequestEdit().getZipCode());
        } else {
            sessionManager.setAddressOne("");
            sessionManager.setAddressTwo("");
            sessionManager.setCity("");
            sessionManager.setState("");
            sessionManager.setCountry("");
            sessionManager.setZipCode("");
        }
    }

    public static final void setUserDetailsInSession(final SessionManager sessionManager, final Profile profile) {
        sessionManager.setUserId(profile.getId());
        sessionManager.setBid(profile.getB_id());
        sessionManager.setUsername(profile.getUserName());
        sessionManager.setFirstName(profile.getFirstName());
        sessionManager.setLastName(profile.getLastName());
        sessionManager.setDescription(profile.getDescription());
        sessionManager.setGender(profile.getGender());
        sessionManager.setEmploymentStatus(profile.getEmployee_status());
        sessionManager.setDob(profile.getDob());

        String imageUrl = "";
        if (profile.getAttachment() != null) {
            final String hashKey = Utils.getMD5Hash(profile.getAttachment());
//            imageUrl = Constants.API_IMAGE_URL(profile.getAttachment(), hashKey, "jpg");
            imageUrl = Constants.AWS_IMAGE_URL(profile.getAttachment(), "jpg");
        } else {
            imageUrl = Constants.NO_IMAGE_URL;
        }

        sessionManager.setUserImage(imageUrl);

        if (profile.getAddress() != null) {
            sessionManager.setAddressOne(profile.getAddress().getAddressLine1());
            sessionManager.setAddressTwo(profile.getAddress().getAddressLine2());
            sessionManager.setCity(profile.getAddress().getCity());
            sessionManager.setState(profile.getAddress().getState());
            sessionManager.setCountry(profile.getAddress().getCountry());
            sessionManager.setZipCode(profile.getAddress().getZipCode());
        } else {
            sessionManager.setAddressOne("");
            sessionManager.setAddressTwo("");
            sessionManager.setCity("");
            sessionManager.setState("");
            sessionManager.setCountry("");
            sessionManager.setZipCode("");
        }
    }
}
