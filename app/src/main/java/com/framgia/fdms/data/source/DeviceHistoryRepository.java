package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.source.remote.DeviceUsingHistoryDataSource;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by framgia on 14/07/2017.
 */
public class DeviceHistoryRepository {
    private static DeviceHistoryRepository sInstances;
    private DeviceUsingHistoryDataSource mDeviceUsingHistoryDataSource;

    public DeviceHistoryRepository(DeviceUsingHistoryDataSource deviceUsingHistoryDataSource) {
        mDeviceUsingHistoryDataSource = deviceUsingHistoryDataSource;
    }

    private DeviceHistoryRepository() {
        mDeviceUsingHistoryDataSource = DeviceUsingHistoryDataSource.getInstances();
    }

    public static DeviceHistoryRepository getInstances() {
        if (sInstances == null) {
            sInstances = new DeviceHistoryRepository();
        }
        return sInstances;
    }

    public Observable<List<DeviceUsingHistory>> getListDeviceHistory() {
        return mDeviceUsingHistoryDataSource.getListDeviceHistory();
    }
}
