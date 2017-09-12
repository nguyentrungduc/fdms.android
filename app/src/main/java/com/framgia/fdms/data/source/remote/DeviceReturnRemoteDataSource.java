package com.framgia.fdms.data.source.remote;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
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

    private DeviceReturnRemoteDataSource() {
        mFDMSApi = FDMSServiceClient.getInstance();
    }

    public static DeviceReturnRemoteDataSource getInstances() {
        if (sInstances == null) sInstances = new DeviceReturnRemoteDataSource();
        return sInstances;
    }

    @Override
    public Observable<List<Status>> getBorrowers() {
        // TODO: later
        List<Status> borrowers = new ArrayList<>();
        borrowers.add(new Status(1, "Chu Anh Tuan"));
        borrowers.add(new Status(2, "Tran Xuan Thang"));
        borrowers.add(new Status(3, "Nguyen Binh Dieu"));
        borrowers.add(new Status(4, "Nguyen Van Tuan"));
        borrowers.add(new Status(5, "Doan Van Toan"));
        borrowers.add(new Status(6, "Nguyen Thi Thuy Dung"));
        return Observable.just(borrowers);
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
