package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.DeviceUsingHistory;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by toand on 9/17/2017.
 */

public class DeviceUsingHistoryRepository implements DeviceUsingHistoryDataSource.RemoteDataSource {
    private DeviceUsingHistoryDataSource.RemoteDataSource mRemoteDataSource;

    public DeviceUsingHistoryRepository(
        DeviceUsingHistoryDataSource.RemoteDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public Observable<List<DeviceUsingHistory>> getListDeviceHistory() {
        return mRemoteDataSource.getListDeviceHistory();
    }
}
