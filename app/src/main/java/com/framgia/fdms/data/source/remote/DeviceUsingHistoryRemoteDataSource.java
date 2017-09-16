package com.framgia.fdms.data.source.remote;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.source.DeviceUsingHistoryDataSource;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by framgia on 14/07/2017.
 */
public class DeviceUsingHistoryRemoteDataSource
    implements DeviceUsingHistoryDataSource.RemoteDataSource {
    private static DeviceUsingHistoryRemoteDataSource sInstances;

    public DeviceUsingHistoryRemoteDataSource() {
    }

    public static DeviceUsingHistoryRemoteDataSource getInstances() {
        if (sInstances == null) {
            sInstances = new DeviceUsingHistoryRemoteDataSource();
        }
        return sInstances;
    }

    @Override
    public Observable<List<DeviceUsingHistory>> getListDeviceHistory() {
        List<DeviceUsingHistory> deviceUsingHistoryList = new ArrayList<>();
        List<Device> deviceList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Device device = new Device(i + "FHN_05_504_0001", i + "sony", i + "Camera");
            deviceList.add(device);
        }
        for (int i = 0; i < 10; i++) {
            DeviceUsingHistory deviceUsingHistory =
                new DeviceUsingHistory(i + "Dat", new Date(), null, deviceList);
            deviceUsingHistoryList.add(deviceUsingHistory);
        }
        return Observable.just(deviceUsingHistoryList);
    }
}
