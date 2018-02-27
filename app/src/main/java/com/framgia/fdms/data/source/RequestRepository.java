package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.AssignmentRequest;
import com.framgia.fdms.data.model.Dashboard;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.api.request.RequestCreatorRequest;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by beepi on 11/05/2017.
 */

public class RequestRepository implements RequestRepositoryContract {
    private static RequestRepository sRequestRepository;
    private RequestDataSource.RemoteDataSource mRemoteDataSource;

    public RequestRepository(RequestDataSource.RemoteDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    public static RequestRepository getInstant(
            RequestDataSource.RemoteDataSource remoteDataSource) {
        if (sRequestRepository == null) {
            sRequestRepository = new RequestRepository(remoteDataSource);
        }
        return sRequestRepository;
    }

    @Override
    public Observable<List<Request>> getMemberRequests(int requestStatusId,
                                                       int relativeId, int page, int perPage) {
        return mRemoteDataSource.getMemberRequests(requestStatusId, relativeId, perPage, page);
    }

    @Override
    public Observable<List<Request>> getMyRequests(int requestStatusId,
                                                   int relativeId, int page, int perPage) {
        return mRemoteDataSource.getMyRequests(requestStatusId, relativeId, page, perPage);
    }

    @Override
    public Observable<List<Status>> getStatus() {
        return mRemoteDataSource.getStatus();
    }

    @Override
    public Observable<List<Dashboard>> getDashboardRequest() {
        return mRemoteDataSource.getDashboardRequest();
    }

    @Override
    public Observable<Request> registerRequest(RequestCreatorRequest request) {
        return mRemoteDataSource.registerRequest(request);
    }

    @Override
    public Observable<List<Request>> getTopRequest(int topRequest) {
        return mRemoteDataSource.getTopRequest(topRequest);
    }

    @Override
    public Observable<Respone<Request>> updateActionRequest(int requestId, int statusId) {
        return mRemoteDataSource.updateActionRequest(requestId, statusId);
    }

    @Override
    public Observable<Respone<Request>> cancelRequest(int requestId, int statusId,
                                                      String description) {
        return mRemoteDataSource.cancelRequest(requestId, statusId, description);
    }

    @Override
    public Observable<Respone<Request>> updateRequest(Request request) {
        return mRemoteDataSource.updateRequest(request);
    }

    @Override
    public Observable<Request> getRequest(int requetsId) {
        return mRemoteDataSource.getRequest(requetsId);
    }

    @Override
    public Observable<Request> assignDeviceForRequest(AssignmentRequest request) {
        return mRemoteDataSource.assignDeviceForRequest(request);
    }

    @Override
    public Observable<String> assignDeviceForNewMember(int staffId, List<Device> items) {
        return mRemoteDataSource.assignDeviceForNewMember(staffId, items);
    }

    @Override
    public Observable<String> assignDeviceForMeetingRoom(int meetingRoomId, List<Device> items) {
        return mRemoteDataSource.assignDeviceForMeetingRoom(meetingRoomId, items);
    }
}
