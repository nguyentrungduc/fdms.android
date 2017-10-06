package com.framgia.fdms.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.framgia.fdms.BR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

/**
 * Created by framgia on 23/05/2017.
 */
public class DeviceUsingHistory extends BaseObservable {
    @Expose
    @SerializedName(value = "staff_name", alternate = { "name", "staff" })
    private String mStaffName;
    @Expose
    @SerializedName("staff_email")
    private String mStaffEmail;
    @Expose
    @SerializedName("from_date")
    private Date mStartDate;
    @Expose
    @SerializedName("return_date")
    private Date mEndDate;
    @SerializedName(value = "device", alternate = { "assignment_details" })
    @Expose
    private List<Device> mUsingDevices;

    @Bindable
    public List<Device> getUsingDevices() {
        return mUsingDevices;
    }

    public void setUsingDevices(List<Device> usingDevices) {
        mUsingDevices = usingDevices;
        notifyPropertyChanged(BR.usingDevices);
    }

    @Bindable
    public String getStaffName() {
        return mStaffName;
    }

    public void setStaffName(String staffName) {
        mStaffName = staffName;
        notifyPropertyChanged(BR.staffName);
    }

    @Bindable
    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
        notifyPropertyChanged(BR.startDate);
    }

    @Bindable
    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
        notifyPropertyChanged(BR.endDate);
    }

    @Bindable
    public String getStaffEmail() {
        return mStaffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        mStaffEmail = staffEmail;
        notifyPropertyChanged(BR.staffEmail);
    }
}
