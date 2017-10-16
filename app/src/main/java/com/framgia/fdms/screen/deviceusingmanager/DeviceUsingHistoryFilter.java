package com.framgia.fdms.screen.deviceusingmanager;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.framgia.fdms.BR;
import com.framgia.fdms.data.model.Status;

import static com.framgia.fdms.utils.Constant.DeviceUsingStatus.ALL;
import static com.framgia.fdms.utils.Constant.DeviceUsingStatus.USING;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * Created by toand on 9/16/2017.
 */

public class DeviceUsingHistoryFilter extends BaseObservable {
    private Status mStatus;
    private String mStaffName;
    private String mDeviceCode;
    private Status mBranch;

    public DeviceUsingHistoryFilter() {
        initDefaultData();
    }

    public void initDefaultData() {
        setDeviceCode("");
        setStaffName("");
        setStatus(new Status(OUT_OF_INDEX, USING));
        setBranch(new Status(OUT_OF_INDEX, ALL));
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

    @Bindable
    public Status getBranch() {
        return mBranch;
    }

    public void setBranch(Status branch) {
        mBranch = branch;
        notifyPropertyChanged(BR.branch);
    }
}
