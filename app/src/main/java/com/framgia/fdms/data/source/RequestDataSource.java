package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.AssignmentItemRequest;
import com.framgia.fdms.data.model.AssignmentRequest;
import com.framgia.fdms.data.model.Dashboard;
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

        Observable<List<Request>> getRequests(int requestType, int requestStatusId, int relativeId,
            int perPage, int page);

        Observable<List<Status>> getStatus();

        Observable<Request> registerRequest(RequestCreatorRequest request);

        Observable<List<Request>> getTopRequest(int topRequest);

        Observable<Respone<Request>> updateActionRequest(int requestId, int actionId);

        Observable<Respone<Request>> cancelRequest(int requestId, int actionId, String description);

        Observable<Respone<Request>> updateRequest(Request request);

        Observable<Request> getRequest(int requetsId);

        Observable<Request> registerAssignment(AssignmentRequest request);

        Observable<String> registerAssignment(int staffId, List<AssignmentItemRequest> items);
    }
}
