package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.remote.BranchRemoteDataSource;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by MyPC on 13/06/2017.
 */

public class BranchRepository implements BranchDataSource {
    private BranchRemoteDataSource mRemoteDataSource;

    public BranchRepository(BranchRemoteDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public Observable<List<Status>> getListBranch() {
        return mRemoteDataSource.getListBranch();
    }
}
