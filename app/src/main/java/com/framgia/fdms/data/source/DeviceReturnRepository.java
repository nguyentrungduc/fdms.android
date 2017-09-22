package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.remote.DeviceReturnRemoteDataSource;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by Hoang Van Nha on 5/23/2017.
 * <></>
 */

public class DeviceReturnRepository {
    private DeviceReturnRemoteDataSource mDataSource;

    public DeviceReturnRepository() {
        mDataSource = DeviceReturnRemoteDataSource.getInstances();
    }

    public Observable<List<Status>> getBorrowers() {
        return mDataSource.getBorrowers();
    }

    public Observable<List<Device>> devicesOfBorrower() {
        return mDataSource.getAssigmentDevices();
    }

    public Observable<String> returnDevice(List<Integer> listDeviceId) {
        return mDataSource.returnDevice(listDeviceId);
    }

    public Observable<List<Device>> getListDevicesOfBorrower(int userId) {
        return mDataSource.getListDevicesOfBorrower(userId);
    }
}
