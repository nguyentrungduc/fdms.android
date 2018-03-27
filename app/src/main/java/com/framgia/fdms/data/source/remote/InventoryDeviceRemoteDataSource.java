package com.framgia.fdms.data.source.remote;

import android.database.Observable;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.source.InventoryDeviceDataSource;

import java.util.List;

/**
 * Created by Sony on 3/27/2018.
 */

public class InventoryDeviceRemoteDataSource implements InventoryDeviceDataSource{
    @Override
    public Observable<List<Device>> postListInventoryDevice(List<Device> devices) {
        return null;
    }
}
