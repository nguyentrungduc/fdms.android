package com.framgia.fdms.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by doan.van.toan on 12/21/17.
 */

public class WSMResponse<T> {
    @Expose
    @SerializedName("status")
    private int mStatus;
    @Expose
    @SerializedName("error")
    private boolean mError;
    @Expose
    @SerializedName("messages")
    private String mMessages;
    @Expose
    @SerializedName("data")
    private T mData;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public boolean isError() {
        return mError;
    }

    public void setError(boolean error) {
        mError = error;
    }

    public String getMessages() {
        return mMessages;
    }

    public void setMessages(String messages) {
        mMessages = messages;
    }

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        mData = data;
    }
}
