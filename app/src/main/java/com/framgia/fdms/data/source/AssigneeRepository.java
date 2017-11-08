package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Status;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by MyPC on 05/05/2017.
 */

public class AssigneeRepository implements AssigneeDataSource {
    private static AssigneeRepository sInstance;

    public static AssigneeRepository getInstance(AssigneeDataSource remoteDataSource) {
        if (sInstance == null) {
            sInstance = new AssigneeRepository(remoteDataSource);
        }
        return sInstance;
    }

    public AssigneeRepository(AssigneeDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    private AssigneeDataSource mRemoteDataSource;

    @Override
    public Observable<List<Status>> getListAssignee() {
        return mRemoteDataSource.getListAssignee();
    }

    @Override
    public Observable<List<Status>> getListAssignee(String name) {
        return mRemoteDataSource.getListAssignee(name);
    }
}
