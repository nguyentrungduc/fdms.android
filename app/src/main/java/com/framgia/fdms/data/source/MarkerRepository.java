package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by framgia on 9/13/17.
 */

public final class MarkerRepository implements MarkerDataSource {
    private static MarkerRepository sInstance;
    private MarkerDataSource mRemoteDataSource;

    public static MarkerRepository getInstance(MarkerDataSource remoteDataSource) {
        if (sInstance == null) {
            sInstance = new MarkerRepository(remoteDataSource);
        }
        return sInstance;
    }

    private MarkerRepository(MarkerDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public Observable<List<Producer>> getListMarker(String name, int page, int perPage) {
        return mRemoteDataSource.getListMarker(name, page, perPage);
    }

    @Override
    public Observable<Producer> addMarker(Producer marker) {
        return mRemoteDataSource.addMarker(marker);
    }

    @Override
    public Observable<String> deleteMarker(Producer marker) {
        return mRemoteDataSource.deleteMarker(marker);
    }

    @Override
    public Observable<String> editMarker(Producer marker) {
        return mRemoteDataSource.editMarker(marker);
    }
}
