package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by beepi on 05/07/2017.
 */
public class MakerRepository implements MakerRepositoryContract {
    private static MakerRepository sMakerRepository;
    private MakerDataSource.RemoteDataSource mRemoteDataSource;

    public MakerRepository(MakerDataSource.RemoteDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    public static MakerRepository getInstant(MakerDataSource.RemoteDataSource remoteDataSource) {
        if (sMakerRepository == null) {
            sMakerRepository = new MakerRepository(remoteDataSource);
        }
        return sMakerRepository;
    }

    @Override
    public Observable<List<Producer>> getMakers(int page, int perpage) {
        return mRemoteDataSource.getListMarkers(page, perpage);
    }
}
