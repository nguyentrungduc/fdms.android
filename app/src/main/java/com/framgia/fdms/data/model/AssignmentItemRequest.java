package com.framgia.fdms.data.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MyPC on 12/09/2017.
 */
public class AssignmentItemRequest {
    @Expose
    @SerializedName("device_id")
    private int mDeviceId;
    @Expose
    @SerializedName("device_category_id")
    private int mDeviceCategoryId;
    @Expose
    @SerializedName("device_category_group_id")
    private int mDeviceCategoryGroupId;
    private String mDeviceCategoryName;
    private String mDeviceCategoryGroupName;
    private String mDeviceCode;

    public AssignmentItemRequest(int deviceId, int deviceCategoryId, int deviceCategoryGroupId,
                             String deviceCategoryName, String deviceCategoryGroupName,
                             String deviceCode) {
        mDeviceId = deviceId;
        mDeviceCategoryId = deviceCategoryId;
        mDeviceCategoryGroupId = deviceCategoryGroupId;
        mDeviceCategoryName = deviceCategoryName;
        mDeviceCategoryGroupName = deviceCategoryGroupName;
        mDeviceCode = deviceCode;
    }

    public int getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(int deviceId) {
        mDeviceId = deviceId;
    }

    public int getDeviceCategoryId() {
        return mDeviceCategoryId;
    }

    public void setDeviceCategoryId(int deviceCategoryId) {
        mDeviceCategoryId = deviceCategoryId;
    }

    public int getDeviceCategoryGroupId() {
        return mDeviceCategoryGroupId;
    }

    public void setDeviceCategoryGroupId(int deviceCategoryGroupId) {
        mDeviceCategoryGroupId = deviceCategoryGroupId;
    }

    public String getDeviceCategoryName() {
        return mDeviceCategoryName;
    }

    public void setDeviceCategoryName(String deviceCategoryName) {
        mDeviceCategoryName = deviceCategoryName;
    }

    public String getDeviceCategoryGroupName() {
        return mDeviceCategoryGroupName;
    }

    public void setDeviceCategoryGroupName(String deviceCategoryGroupName) {
        mDeviceCategoryGroupName = deviceCategoryGroupName;
    }

    public String getDeviceCode() {
        return mDeviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        mDeviceCode = deviceCode;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * Class Builder
     */
    public static class Builder {
        private int mNestedDeviceId;
        private int mNestedDeviceCategoryId;
        private int mNestedCategoryGroupId;
        private String mNestedDeviceCategoryName;
        private String mNestedDeviceCategoryGroupName;
        private String mNestedDeviceCode;

        public Builder setNestedDeviceId(int nestedDeviceId) {
            mNestedDeviceId = nestedDeviceId;
            return this;
        }

        public Builder setNestedDeviceCategoryId(int nestedDeviceCategoryId) {
            mNestedDeviceCategoryId = nestedDeviceCategoryId;
            return this;
        }

        public Builder setNestedCategoryGroupId(int nestedCategoryGroupId) {
            mNestedCategoryGroupId = nestedCategoryGroupId;
            return this;
        }

        public Builder setNestedDeviceCategoryName(String nestedDeviceCategoryName) {
            mNestedDeviceCategoryName = nestedDeviceCategoryName;
            return this;
        }

        public Builder setNestedDeviceCategoryGroupName(String nestedDeviceCategoryGroupName) {
            mNestedDeviceCategoryGroupName = nestedDeviceCategoryGroupName;
            return this;
        }

        public Builder setNestedDeviceCode(String nestedDeviceCode) {
            mNestedDeviceCode = nestedDeviceCode;
            return this;
        }

        public AssignmentItemRequest create() {
            return new AssignmentItemRequest(mNestedDeviceId,
                mNestedDeviceCategoryId,
                mNestedCategoryGroupId,
                mNestedDeviceCategoryName,
                mNestedDeviceCategoryGroupName,
                mNestedDeviceCode);
        }
    }
}
