package com.framgia.fdms.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Hoang Van Nha on 5/23/2017.
 * <></>
 */

public class DeviceHistoryDetail extends BaseObservable {
    private final String STATUS_USING = "using";
    private final String STATUS_AVAILABLE = "available";
    private final String STATUS_BROKEN = "broken";

    @Expose
    @SerializedName("history_infor")
    private DeviceHistoryInfo mHistoryInfo;
    @Expose
    @SerializedName("device_content")
    private Device mDevice;

    public int getStatusImage() {
        if (mHistoryInfo == null
            || mHistoryInfo.getHistoryData() == null
            || mHistoryInfo.getHistoryData().getStatus() == null) {
            return R.drawable.ic_avaiable;
        }
        switch (mHistoryInfo.getHistoryData().getStatus()) {
            case STATUS_USING:
                return R.drawable.ic_using;
            case STATUS_AVAILABLE:
                return R.drawable.ic_avaiable;
            case STATUS_BROKEN:
                return R.drawable.ic_broken;
            default:
                return R.drawable.ic_avaiable;
        }
    }

    @Bindable
    public DeviceHistoryInfo getHistoryInfo() {
        return mHistoryInfo;
    }

    public void setHistoryInfo(DeviceHistoryInfo historyInfo) {
        mHistoryInfo = historyInfo;
        notifyPropertyChanged(BR.historyInfo);
    }

    @Bindable
    public Device getDevice() {
        return mDevice;
    }

    public void setDevice(Device device) {
        mDevice = device;
        notifyPropertyChanged(BR.device);
    }
}
