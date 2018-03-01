package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.AssignmentRequest;
import com.framgia.fdms.data.model.Dashboard;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.api.request.RequestCreatorRequest;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by beepi on 11/05/2017.
 */

public interface RequestDataSource {
    interface LocalDataSource {
    }

    interface RemoteDataSource {
        Observable<List<Dashboard>> getDashboardRequest();

        /**
         * Get member request filter by
         * @param requestStatusId
         * @param relativeId
         * @param page
         * @param perPage
         * @return
         */
        Observable<List<Request>> getMemberRequests(int requestStatusId, int relativeId,
                                                    int page, int perPage);

        /**
         * get request was created for current user filter by
         * @param requestStatusId
         * @param relativeId
         * @param page
         * @param perPage
         * @return
         */
        Observable<List<Request>> getMyRequests(int requestStatusId, int relativeId,
                                                    int page, int perPage);

        Observable<List<Status>> getStatus();

        Observable<Request> registerRequestForMember(RequestCreatorRequest request);

        Observable<Request> registerMyRequest(RequestCreatorRequest request);

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
