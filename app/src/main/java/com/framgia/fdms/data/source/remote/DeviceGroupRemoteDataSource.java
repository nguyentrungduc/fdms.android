package com.framgia.fdms.data.source.remote;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.source.DeviceGroupDataSource;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by toand on 9/25/2017.
 */

public class DeviceGroupRemoteDataSource extends BaseRemoteDataSource
    implements DeviceGroupDataSource {
    private static final DeviceGroupRemoteDataSource sInstance = new DeviceGroupRemoteDataSource();

    public DeviceGroupRemoteDataSource() {
        super(FDMSServiceClient.getInstance());
    }

    public static DeviceGroupRemoteDataSource getInstance() {
        return sInstance;
    }

    @Override
    public Observable<List<Producer>> getListDeviceGroup(String name, int page, int perPage) {
        // TODO: 9/25/2017
        return Observable.error(new NullPointerException());
    }

    @Override
    public Observable<Producer> addDeviceGroup(Producer deviceGroup) {
        // TODO: 9/25/2017
        return Observable.error(new NullPointerException());
    }

    @Override
    public Observable<Respone<String>> deleteDeviceGroup(Producer deviceGroup) {
        // TODO: 9/25/2017
        return Observable.error(new NullPointerException());
    }

    @Override
    public Observable<String> editDeviceGroup(Producer deviceGroup) {
        // TODO: 9/25/2017
        return Observable.error(new NullPointerException());
    }
}
