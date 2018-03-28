package com.framgia.fdms.data.source.remote;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoang Van Nha on 5/23/2017.
 * <></>
 */
public class DeviceReturnRemoteDataSource
    implements com.framgia.fdms.data.source.DeviceReturnDataSource.RemoteDataSource {
    private static DeviceReturnRemoteDataSource sInstances;
    private FDMSApi mFDMSApi;

    public DeviceReturnRemoteDataSource() {
        mFDMSApi = FDMSServiceClient.getInstance();
    }

    public static DeviceReturnRemoteDataSource getInstances() {
        if (sInstances == null) sInstances = new DeviceReturnRemoteDataSource();
        return sInstances;
    }

    @Override
    public Observable<List<Device>> getDevicesOfBorrower() {
        return mFDMSApi.getDevicesBorrow()
            .flatMap(new Function<Respone<List<Device>>, ObservableSource<List<Device>>>() {
                @Override
                public ObservableSource<List<Device>> apply(Respone<List<Device>> listRespone)
                    throws Exception {
                    return Utils.getResponse(listRespone);
                }
            });
    }

    @Override
    public Observable<String> returnDevice(List<Integer> listDeviceId) {
        return mFDMSApi.returnDevice(listDeviceId).flatMap(new Function<Respone<String>,
            ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Respone<String> stringRespone) throws Exception {
                if (stringRespone == null) {
                    return Observable.error(new NullPointerException());
                } else if (stringRespone.isError()) {
                    return Observable.error(new NullPointerException(stringRespone.getMessage()));
                } else {
                    return Observable.just(stringRespone.getMessage());
                }
            }
        });
    }

    @Override
    public Observable<List<Device>> getListDevicesOfBorrower(int userId) {
        return mFDMSApi.getListDeviceOfUserBorrow(userId)
            .flatMap(new Function<Respone<List<Device>>, ObservableSource<List<Device>>>() {
                @Override
                public ObservableSource<List<Device>> apply(
                    @NonNull Respone<List<Device>> listRespone) throws Exception {
                    return Utils.getResponse(listRespone);
                }
            });
    }

    public Observable<List<Device>> getAssigmentDevices() {
        List<Device> devices = new ArrayList<>();
        Device device = new Device();
        device.setProductionName("Camera");
        int i = 0;
        while (i < 10) {
            devices.add(device);
            i++;
        }
        return Observable.just(devices);
    }
}
