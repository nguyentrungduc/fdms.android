package com.framgia.fdms.data.source.remote;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.source.VendorDataSource;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import java.util.List;

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
            .flatMap(new Function<Respone<List<Producer>>, ObservableSource<List<Producer>>>() {
                @Override
                public ObservableSource<List<Producer>> apply(Respone<List<Producer>> listRespone)
                    throws Exception {
                    return Utils.getResponse(listRespone);
                }
            });
    }

    @Override
    public Observable<Producer> addVendor(Producer producer) {
        return mFDMSApi.addVendor(producer.getName(), producer.getDescription())
            .flatMap(new Function<Respone<Producer>, ObservableSource<Producer>>() {
                @Override
                public ObservableSource<Producer> apply(Respone<Producer> producerRespone)
                    throws Exception {
                    return Utils.getResponse(producerRespone);
                }
            });
    }

    @Override
    public Observable<Respone<String>> deleteVendor(Producer producer) {
        return mFDMSApi.deleteVendor(producer.getId());
    }

    @Override
    public Observable<String> editVendor(Producer producer) {
        return mFDMSApi.updateVendor(producer.getId(), producer.getName(),
            producer.getDescription())
            .flatMap(new Function<Respone<List<String>>, ObservableSource<String>>() {
                @Override
                public ObservableSource<String> apply(Respone<List<String>> listRespone)
                    throws Exception {
                    if (listRespone == null) {
                        return Observable.error(new NullPointerException());
                    }
                    if (listRespone.isError()) {
                        return Observable.error(
                            new NullPointerException("ERROR" + listRespone.getStatus()));
                    }
                    return Observable.just(getStringFromList(listRespone.getData()));
                }
            });
    }

    public String getStringFromList(List<String> strings) {
        if (strings == null || strings.size() == 0) {
            return "";
        }
        String result = "";
        for (String str : strings) {
            result += str + "\n";
        }
        return result;
    }
}
