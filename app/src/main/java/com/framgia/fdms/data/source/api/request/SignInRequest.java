package com.framgia.fdms.data.source.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by doan.van.toan on 12/21/17.
 */

public class SignInRequest extends BaseRequest {
    @Expose
    @SerializedName("sign_in")
    private SignIn mSignIn;

    public SignInRequest(String username, String password, String deviceId) {
        mSignIn = new SignIn(username, password, deviceId);
    }

    private static class SignIn {
        @Expose
        @SerializedName("email")
        private String mEmail;
        @Expose
        @SerializedName("password")
        private String mPassword;
        @Expose
        @SerializedName("device_id")
        private String mDeviceId;

        SignIn(String email, String password, String deviceId) {
            mEmail = email;
            mPassword = password;
            mDeviceId = deviceId;
        }

        public String getEmail() {
            return mEmail;
        }

        public void setEmail(String email) {
            mEmail = email;
        }

        public String getPassword() {
            return mPassword;
        }

        public void setPassword(String password) {
            mPassword = password;
        }

        public String getDeviceId() {
            return mDeviceId;
        }

        public void setDeviceId(String deviceId) {
            mDeviceId = deviceId;
        }
    }
}