package com.framgia.fdms.screen.inventorydevice;

import android.databinding.BaseObservable;

import com.framgia.fdms.data.model.Device;

/**
 * Created by Sony on 3/27/2018.
 */

public class ItemDeviceInventoryViewModel extends BaseObservable implements OnDeviceListenner {

    private Device mDevice;

    public ItemDeviceInventoryViewModel(Device device) {
        mDevice = device;
    }

    @Override
    public void onDeviceClick() {

    }
}
