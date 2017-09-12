package com.framgia.fdms.data.source.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by framgia on 11/05/2017.
 */

public class ErrorResponse {
    @SerializedName("status")
    @Expose
    private int mStatus;
    @SerializedName("message")
    @Expose
    private String mMessage;
    @SerializedName("error")
    @Expose
    private boolean mError;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public boolean isError() {
        return mError;
    }

    public void setError(boolean error) {
        mError = error;
    }
}
