package com.framgia.fdms.screen.inventorydevice;

import com.framgia.fdms.data.model.Device;

/**
 * Created by Sony on 3/27/2018.
 */

public interface OnDeviceListenner {
    void onDeviceClick(Device device, int position);

    void onDeviceCommentClick(Device device, int position);

}
