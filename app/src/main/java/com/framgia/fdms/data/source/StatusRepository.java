package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.AssigneeUser;
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

    public Observable<List<Status>> getListStatusRequest(String query) {
        return mStatusRemoteDataSource.getListStatusRequest(query);
    }

    public Observable<List<Status>> getListStatusEditRequest(int requestStatusId, String query) {
        return mStatusRemoteDataSource.getListStatusEditRequest(requestStatusId, query);
    }

    public Observable<List<AssigneeUser>> getListRelative(String query, int page, int perPage) {
        return mStatusRemoteDataSource.getListRelative(query, page, perPage);
    }

    public Observable<List<Status>> getListAssignee() {
        return mStatusRemoteDataSource.getListAssignee();
    }

    public Observable<List<Status>> getListUserBorrow(String query, int page, int perPage) {
        return mStatusRemoteDataSource.getListUserBorrow(query, page, perPage);
    }
}
