package com.framgia.fdms.data.source.remote;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.source.VendorDataSource;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.utils.Utils;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by framgia on 03/07/2017.
 */
public final class VendorRemoteDataSource extends BaseRemoteDataSource
        implements VendorDataSource.RemoteDataSource {
    private static VendorRemoteDataSource sInstances;

    public VendorRemoteDataSource(FDMSApi api) {
        super(api);
    }

    public static VendorRemoteDataSource getInstances(FDMSApi api) {
        if (sInstances == null) {
            sInstances = new VendorRemoteDataSource(api);
        }
        return sInstances;
    }

    @Override
    public Observable<List<Producer>> getListVendor(int page, int perPage) {
        return mFDMSApi.getListVendors(page, perPage)
                .flatMap(new Func1<Respone<List<Producer>>, Observable<List<Producer>>>() {
                    @Override
                    public Observable<List<Producer>> call(Respone<List<Producer>> listRespone) {
                        return Utils.getResponse(listRespone);
                    }
                });
    }

    @Override
    public Observable<Void> addVendor(Producer producer) {
        return Observable.just(null);
    }

    @Override
    public Observable<Void> deleteVendor(Producer producer) {
        return Observable.just(null);
    }

    @Override
    public Observable<Void> editVendor(Producer producer) {
        return Observable.just(null);
    }
}
