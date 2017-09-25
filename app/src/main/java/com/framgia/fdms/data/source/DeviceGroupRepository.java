package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.source.remote.DeviceGroupRemoteDataSource;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by toand on 9/25/2017.
 */

public final class DeviceGroupRepository implements DeviceGroupDataSource {
    private static final DeviceGroupRepository sInstance = new DeviceGroupRepository();

    private DeviceGroupDataSource mRemoteDataSource;

    public static DeviceGroupRepository getInstance() {
        return sInstance;
    }

    private DeviceGroupRepository() {
        mRemoteDataSource = DeviceGroupRemoteDataSource.getInstance();
    }

    @Override
    public Observable<List<Producer>> getListDeviceGroup(String name, int page, int perPage) {
        return mRemoteDataSource.getListDeviceGroup(name, page, perPage);
    }

    @Override
    public Observable<Producer> addDeviceGroup(Producer deviceGroup) {
        return mRemoteDataSource.addDeviceGroup(deviceGroup);
    }

    @Override
    public Observable<Respone<String>> deleteDeviceGroup(Producer deviceGroup) {
        return mRemoteDataSource.deleteDeviceGroup(deviceGroup);
    }

    @Override
    public Observable<String> editDeviceGroup(Producer deviceGroup) {
        return mRemoteDataSource.editDeviceGroup(deviceGroup);
    }
}
