package com.framgia.fdms.data.source.remote;

import android.text.TextUtils;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.source.DeviceGroupDataSource;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    public Observable<List<Producer>> getListDeviceGroup(final String name) {
        if (TextUtils.isEmpty(name)) {
            return getListDeviceGroup();
        }
        return getListDeviceGroup().flatMap(
            new Function<List<Producer>, ObservableSource<List<Producer>>>() {
                @Override
                public ObservableSource<List<Producer>> apply(List<Producer> statuses)
                    throws Exception {
                    List<Producer> data = new ArrayList<>();
                    for (Producer status : statuses) {
                        if (status.getName()
                            .toLowerCase(Locale.getDefault())
                            .contains(name.toLowerCase(Locale.getDefault()))) {
                            data.add(status);
                        }
                    }
                    return Observable.just(data);
                }
            });
    }

    private Observable<List<Producer>> getListDeviceGroup() {
        return mFDMSApi.getDeviceGroups()
            .flatMap(new Function<Respone<List<Producer>>, ObservableSource<List<Producer>>>() {
                @Override
                public ObservableSource<List<Producer>> apply(
                    @NonNull Respone<List<Producer>> listRespone) throws Exception {
                    return Utils.getResponse(listRespone);
                }
            });
    }

    @Override
    public Observable<Producer> addDeviceGroup(Producer deviceGroup) {
        return mFDMSApi.addDeviceGroup(deviceGroup.getName(), deviceGroup.getDescription())
            .flatMap(new Function<Respone<Producer>, ObservableSource<Producer>>() {
                @Override
                public ObservableSource<Producer> apply(Respone<Producer> producerRespone)
                    throws Exception {
                    return Utils.getResponse(producerRespone);
                }
            });
    }

    @Override
    public Observable<Respone<String>> deleteDeviceGroup(Producer deviceGroup) {
        return mFDMSApi.deleteDeviceGroup(deviceGroup.getId());
    }

    @Override
    public Observable<String> editDeviceGroup(Producer deviceGroup) {
        return mFDMSApi.editDeviceGroup(deviceGroup.getId(), deviceGroup.getName(),
            deviceGroup.getDescription())
            .flatMap(new Function<Respone<Producer>, ObservableSource<String>>() {
                @Override
                public ObservableSource<String> apply(Respone<Producer> producerRespone)
                    throws Exception {
                    if (producerRespone == null) {
                        return Observable.error(new NullPointerException());
                    } else if (producerRespone.isError()) {
                        return Observable.error(
                            new NullPointerException("ERROR" + producerRespone.getMessage()));
                    } else {
                        return Observable.just(producerRespone.getMessage());
                    }
                }
            });
    }
}
