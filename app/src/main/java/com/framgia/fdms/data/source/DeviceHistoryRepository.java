package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.source.remote.DeviceUsingHistoryRemoteDataSource;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by framgia on 14/07/2017.
 */
public class DeviceHistoryRepository {
    private static DeviceHistoryRepository sInstances;
    private DeviceUsingHistoryRemoteDataSource mDeviceUsingHistoryDataSource;

    public DeviceHistoryRepository(
        DeviceUsingHistoryRemoteDataSource deviceUsingHistoryDataSource) {
        mDeviceUsingHistoryDataSource = deviceUsingHistoryDataSource;
    }

    private DeviceHistoryRepository() {
        mDeviceUsingHistoryDataSource = DeviceUsingHistoryRemoteDataSource.getInstances();
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
