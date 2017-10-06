package com.framgia.fdms.screen.device.listdevice;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.framgia.fdms.BR;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Status;

import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.TITLE_NA;

/**
 * Created by toand on 9/15/2017.
 */

public class DeviceFilterModel extends BaseObservable {
    private String mStaffName;
    private Status mStatus;
    private Status mCategory;
    private Status mBranch;
    private Producer mVendor;
    private Producer mMarker;
    private Producer mMeetingRoom;
    private String mDeviceName;

    public DeviceFilterModel() {
        initDefaultFilter();
    }

    public void initDefaultFilter() {
        setCategory(new Status(OUT_OF_INDEX, TITLE_NA));
        setStatus(new Status(OUT_OF_INDEX, TITLE_NA));
        setMeetingRoom(new Producer(OUT_OF_INDEX, TITLE_NA));
        setVendor(new Producer(OUT_OF_INDEX, TITLE_NA));
        setMarker(new Producer(OUT_OF_INDEX, TITLE_NA));
        setBranch(new Status(OUT_OF_INDEX, ""));
        setDeviceName("");
        setStaffName("");
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
    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public Status getCategory() {
        return mCategory;
    }

    public void setCategory(Status category) {
        mCategory = category;
        notifyPropertyChanged(BR.category);
    }

    @Bindable
    public Producer getVendor() {
        return mVendor;
    }

    public void setVendor(Producer vendor) {
        mVendor = vendor;
        notifyPropertyChanged(BR.vendor);
    }

    @Bindable
    public Producer getMarker() {
        return mMarker;
    }

    public void setMarker(Producer marker) {
        mMarker = marker;
        notifyPropertyChanged(BR.marker);
    }

    @Bindable
    public Producer getMeetingRoom() {
        return mMeetingRoom;
    }

    public void setMeetingRoom(Producer meetingRoom) {
        mMeetingRoom = meetingRoom;
        notifyPropertyChanged(BR.meetingRoom);
    }

    @Bindable
    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        mDeviceName = deviceName;
        notifyPropertyChanged(BR.deviceName);
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
