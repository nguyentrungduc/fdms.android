package com.framgia.fdms.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by doan.van.toan on 12/21/17.
 */

public class WSMUserResponse {
    @SerializedName("user_info")
    @Expose
    private UserInfo mUserInfo;
    @SerializedName("authen_token")
    @Expose
    private String mAuthenToken;
    @SerializedName("is_manager")
    @Expose
    private boolean mIsManager;

    public class UserInfo {
        @SerializedName("id")
        @Expose
        private int mId;
        @SerializedName("name")
        @Expose
        private String mName;
        @SerializedName("email")
        @Expose
        private String mEmail;

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getEmail() {
            return mEmail;
        }

        public void setEmail(String email) {
            mEmail = email;
        }
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    public String getAuthenToken() {
        return mAuthenToken;
    }

    public void setAuthenToken(String authenToken) {
        mAuthenToken = authenToken;
    }

    public boolean isManager() {
        return mIsManager;
    }

    public void setManager(boolean manager) {
        mIsManager = manager;
    }
}
