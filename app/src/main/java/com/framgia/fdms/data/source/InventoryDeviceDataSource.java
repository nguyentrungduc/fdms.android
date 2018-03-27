package com.framgia.fdms.data.source;

import android.database.Observable;

import com.framgia.fdms.data.model.Device;

import java.util.List;

/**
 * Created by Sony on 3/27/2018.
 */

public interface InventoryDeviceDataSource {
    Observable<List<Device>> postListInventoryDevice(List<Device> devices);
}
