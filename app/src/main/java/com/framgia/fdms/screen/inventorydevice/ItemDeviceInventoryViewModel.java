package com.framgia.fdms.screen.inventorydevice;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.fdms.BR;
import com.framgia.fdms.data.model.Device;

/**
 * Created by Sony on 3/27/2018.
 */

public class ItemDeviceInventoryViewModel extends BaseObservable {
    private Device mDevice;
    private int mPosition;
    private OnDeviceListenner mOnDeviceListenner;

    public ItemDeviceInventoryViewModel(OnDeviceListenner onDeviceListenner, Device device,
                                        int position) {
        mDevice = device;
        mOnDeviceListenner = onDeviceListenner;
        mPosition = position;
    }


    public void onDeviceClick() {
        mOnDeviceListenner.onDeviceClick(mDevice, mPosition);

    }

    public void onDeviceCommentClick() {
        mOnDeviceListenner.onDeviceCommentClick(mDevice, mPosition);
    }

    public String getProductionName() {
        return mDevice.getProductionName() != null ? mDevice.getProductionName() : "";
    }

    public String getDeviceCode() {
        return mDevice.getDeviceCode() != null ? mDevice.getDeviceCode() : "";
    }

    public boolean getSelected() {
        return mDevice.isSelected();
    }
}