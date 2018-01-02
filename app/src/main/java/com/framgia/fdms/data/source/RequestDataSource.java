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

public interface RequestDataSource {
    interface LocalDataSource {
    }

    interface RemoteDataSource {
        Observable<List<Dashboard>> getDashboardRequest();

        Observable<List<Request>> getRequests(int requestType, int requestStatusId, int relativeId,
                                              int perPage, int page);

        Observable<List<Status>> getStatus();

        Observable<Request> registerRequest(RequestCreatorRequest request);

        Observable<List<Request>> getTopRequest(int topRequest);

        Observable<Respone<Request>> updateActionRequest(int requestId, int actionId);

        Observable<Respone<Request>> cancelRequest(int requestId, int actionId, String description);

        Observable<Respone<Request>> updateRequest(Request request);

        Observable<Request> getRequest(int requetsId);

        Observable<Request> assignDeviceForRequest(AssignmentRequest request);

        Observable<String> assignDeviceForNewMember(int staffId, List<Device> items);

        Observable<String> assignDeviceForMeetingRoom(int meetingRoomId, List<Device> items);
    }
}
