package com.framgia.fdms.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by doan.van.toan on 1/8/18.
 */

public class NotificationResult {
    @SerializedName("count_unread_messages")
    @Expose
    private int mCountUnreadMesssages;
    @SerializedName("notifications")
    @Expose
    private List<Notification> mNotifications;

    public int getCountUnreadMesssages() {
        return mCountUnreadMesssages;
    }

    public void setCountUnreadMesssages(int countUnreadMesssages) {
        mCountUnreadMesssages = countUnreadMesssages;
    }

    public List<Notification> getNotifications() {
        return mNotifications;
    }

    public void setNotifications(List<Notification> notifications) {
        mNotifications = notifications;
    }
}
