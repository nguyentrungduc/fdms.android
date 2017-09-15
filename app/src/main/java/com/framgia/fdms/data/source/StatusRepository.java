package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.remote.StatusRemoteDataSource;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by MyPC on 05/05/2017.
 */

public class StatusRepository {
    private StatusRemoteDataSource mStatusRemoteDataSource;

    public StatusRepository(StatusRemoteDataSource statusRemoteDataSource) {
        mStatusRemoteDataSource = statusRemoteDataSource;
    }

    public Observable<List<Status>> getListStatus() {
        return mStatusRemoteDataSource.getListStatus();
    }

    public Observable<List<Status>> getListStatus(String query) {
        return mStatusRemoteDataSource.getListStatus(query);
    }

    public Observable<List<Status>> getListStatusRequest() {
        return mStatusRemoteDataSource.getListStatusRequest();
    }

    public Observable<List<Status>> getListRelative() {
        return mStatusRemoteDataSource.getListRelative();
    }

    public Observable<List<Status>> getListAssignee() {
        return mStatusRemoteDataSource.getListAssignee();
    }
}
