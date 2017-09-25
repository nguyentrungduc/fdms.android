package com.framgia.fdms.screen.deviceusingmanager;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.framgia.fdms.BR;
import com.framgia.fdms.data.model.Status;

import static com.framgia.fdms.utils.Constant.DeviceUsingStatus.USING;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * Created by toand on 9/16/2017.
 */

public class DeviceUsingHistoryFilter extends BaseObservable {
    private Status mStatus;
    private String mStaffName;
    private String mDeviceCode;

    public DeviceUsingHistoryFilter() {
        initDefaultData();
    }

    public void initDefaultData() {
        setDeviceCode("");
        setStaffName("");
        setStatus(new Status(OUT_OF_INDEX, USING));
    }

    @Bindable
    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
        notifyPropertyChanged(BR.status);
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
    public String getDeviceCode() {
        return mDeviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        mDeviceCode = deviceCode;
        notifyPropertyChanged(BR.deviceCode);
    }
}
