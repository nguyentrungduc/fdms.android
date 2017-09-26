package com.framgia.fdms.screen.meetingroom.detailmeetingroom;

import android.databinding.BaseObservable;
import android.view.View;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.data.model.Device;

/**
 * Created by ASUS on 11/09/2017.
 */

public class ItemListDeviceViewModel extends BaseObservable {
    private Device mDevice;
    private final BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Device>
        mItemClickListener;

    public ItemListDeviceViewModel(Device device,
        BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Device> itemClickListener) {
        mDevice = device;
        mItemClickListener = itemClickListener;
    }

    public void onItemClicked(View view) {
        if (mItemClickListener == null) {
            return;
        }
        mItemClickListener.onItemRecyclerViewClick(mDevice);
    }

    public String getDeviceName() {
        return mDevice != null ? mDevice.getProductionName() : "";
    }

    public String getDeviceCode() {
        return mDevice != null ? mDevice.getDeviceCode() : "";
    }

    public String getDeviceCategory() {
        return mDevice != null ? mDevice.getDeviceCategoryName() : "";
    }
}
