package com.framgia.fdms.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.fdms.BR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 *
 */

public class AssignmentResponse extends BaseObservable {
    @SerializedName("id")
    @Expose
    private int mId;
    @SerializedName("return_date")
    @Expose
    private Date mReturnDate;
    @SerializedName("borrow_date")
    @Expose
    private Date mBorrowDate;
    @SerializedName("device_id")
    @Expose
    private int mDeviceId;
    @SerializedName("device_category_name")
    @Expose
    private String mDeviceCategoryName;
    @SerializedName("device_code")
    @Expose
    private String mDeviceCode;

    @Bindable
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public Date getReturnDate() {
        return mReturnDate;
    }

    public void setReturnDate(Date returnDate) {
        mReturnDate = returnDate;
        notifyPropertyChanged(BR.returnDate);
    }

    @Bindable
    public Date getBorrowDate() {
        return mBorrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        mBorrowDate = borrowDate;
        notifyPropertyChanged(BR.borrowDate);
    }

    @Bindable
    public int getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(int deviceId) {
        mDeviceId = deviceId;
        notifyPropertyChanged(BR.deviceId);
    }

    @Bindable
    public String getDeviceCategoryName() {
        return mDeviceCategoryName;
    }

    public void setDeviceCategoryName(String deviceCategoryName) {
        mDeviceCategoryName = deviceCategoryName;
        notifyPropertyChanged(BR.deviceCategoryName);
    }

    @Bindable
    public String getDeviceCode() {
        return mDeviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        mDeviceCode = deviceCode;
        notifyPropertyChanged(BR.deviceCode);
    }
}
