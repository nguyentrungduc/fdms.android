package com.framgia.fdms.data.source.remote;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.source.MarkerDataSource;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import java.util.List;

/**
 * Created by framgia on 9/13/17.
 */

public class MarkerRemoteDataSource extends BaseRemoteDataSource implements MarkerDataSource {
    private static MarkerRemoteDataSource sInstance;

    public static MarkerRemoteDataSource getInstance(FDMSApi fdmsApi) {
        if (sInstance == null) {
            sInstance = new MarkerRemoteDataSource(fdmsApi);
        }
        return sInstance;
    }

    public MarkerRemoteDataSource(FDMSApi fdmsApi) {
        super(fdmsApi);
    }

    @Override
    public Observable<List<Producer>> getListMarker(String name, int page, int perPage) {
        return mFDMSApi.getMakers(name, page, perPage)
            .flatMap(new Function<Respone<List<Producer>>, ObservableSource<List<Producer>>>() {
                @Override
                public ObservableSource<List<Producer>> apply(Respone<List<Producer>> listRespone)
                    throws Exception {
                    return Utils.getResponse(listRespone);
                }
            });
    }

    @Override
    public Observable<Producer> addMarker(Producer marker) {
        return null;
    }

    @Override
    public Observable<Respone<String>> deleteMarker(Producer marker) {
        return null;
    }

    @Override
    public Observable<String> editMarker(Producer marker) {
        return null;
    }
}
