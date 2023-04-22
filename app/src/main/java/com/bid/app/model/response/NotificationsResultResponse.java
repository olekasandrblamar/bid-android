package com.bid.app.model.response;

import androidx.annotation.NonNull;

import com.bid.app.model.view.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.List;

public class NotificationsResultResponse {

    @JsonProperty("error")
    private ErrorStatus errorStatus;

    @JsonProperty("data")
    private List<NotificationListInfo> notificationListInfo;

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(ErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
    }

    public List<NotificationListInfo> getNotificationListInfo() {
        return notificationListInfo;
    }

    public void setNotificationListInfo(List<NotificationListInfo> notificationListInfo) {
        this.notificationListInfo = notificationListInfo;
    }

    @NonNull
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
