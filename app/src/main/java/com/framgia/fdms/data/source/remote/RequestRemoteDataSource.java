package com.framgia.fdms.data.source.remote;

import android.text.TextUtils;
import com.framgia.fdms.data.model.AssignmentItemRequest;
import com.framgia.fdms.data.model.AssignmentRequest;
import com.framgia.fdms.data.model.Dashboard;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.RequestDataSource;
import com.framgia.fdms.data.source.api.request.RequestCreatorRequest;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.framgia.fdms.utils.Constant.ALL_RELATIVE_ID;
import static com.framgia.fdms.utils.Constant.ALL_REQUEST_STATUS_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.ASSIGNMENT_ASSIGNEE_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.ASSIGNMENT_ASSIGNMENT_DETAILS;
import static com.framgia.fdms.utils.Constant.ApiParram.ASSIGNMENT_ASSIGNMENT_DEVICE_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.ASSIGNMENT_DESCRIPTION;
import static com.framgia.fdms.utils.Constant.ApiParram.ASSIGNMENT_REQUEST_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.ASSIGNMENT_STAFF_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.PAGE;
import static com.framgia.fdms.utils.Constant.ApiParram.PER_PAGE;
import static com.framgia.fdms.utils.Constant.ApiParram.RELATIVE_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_ASSIGNEE_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_DESCRIPTION;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_FOR_USER_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_REQUEST_DETAILS;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_STATUS_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_TITLE;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_TYPE;
import static com.framgia.fdms.utils.Constant.BundleRequestType.MY_REQUEST;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * Created by beepi on 11/05/2017.
 */

public class RequestRemoteDataSource extends BaseRemoteDataSource
    implements RequestDataSource.RemoteDataSource {
    private FDMSApi mFDMSApi;

    public RequestRemoteDataSource(FDMSApi FDMSApi) {
        super(FDMSApi);
        mFDMSApi = FDMSApi;
    }

    @Override
    public Observable<List<Request>> getRequests(int requestType, int requestStatusId,
        int relativeId, int page, int perPage) {
        return mFDMSApi.getRequests(
            getRequestParams(requestType, requestStatusId, relativeId, page, perPage))
            .flatMap(new Function<Respone<List<Request>>, ObservableSource<List<Request>>>() {
                @Override
                public ObservableSource<List<Request>> apply(Respone<List<Request>> listRespone)
                    throws Exception {
                    return Utils.getResponse(listRespone);
                }
            });
    }

    @Override
    public Observable<List<Status>> getStatus() {
        List<Status> status = new ArrayList<>();
        return Observable.just(status);
    }

    @Override
    public Observable<Request> registerRequest(RequestCreatorRequest request) {
        Map<String, String> parrams = new HashMap<>();

        if (!TextUtils.isEmpty(request.getTitle())) {
            parrams.put(REQUEST_TITLE, request.getTitle());
        }
        if (!TextUtils.isEmpty(request.getDescription())) {
            parrams.put(REQUEST_DESCRIPTION, request.getDescription());
        }
        if (request.getRequestFor() > 0) {
            parrams.put(REQUEST_FOR_USER_ID, String.valueOf(request.getRequestFor()));
        }
        if (request.getAssignee() > 0) {
            parrams.put(REQUEST_ASSIGNEE_ID, String.valueOf(request.getAssignee()));
        }
        return mFDMSApi.registerRequest(parrams)
            .flatMap(new Function<Respone<Request>, ObservableSource<Request>>() {
                @Override
                public ObservableSource<Request> apply(Respone<Request> requestRespone)
                    throws Exception {
                    return Utils.getResponse(requestRespone);
                }
            });
    }

    @Override
    public Observable<List<Request>> getTopRequest(int topRequest) {
        return mFDMSApi.getTopRequest(topRequest)
            .flatMap(new Function<Respone<List<Request>>, ObservableSource<List<Request>>>() {
                @Override
                public ObservableSource<List<Request>> apply(Respone<List<Request>> listRespone)
                    throws Exception {
                    return Utils.getResponse(listRespone);
                }
            });
    }

    @Override
    public Observable<Respone<Request>> updateActionRequest(int requestId, int actionId) {
        return mFDMSApi.updateActionRequest(requestId, actionId);
    }

    @Override
    public Observable<Respone<Request>> cancelRequest(int requestId, int actionId,
        String description) {
        return mFDMSApi.cancelRequest(requestId, actionId, description);
    }

    @Override
    public Observable<Respone<Request>> updateRequest(Request request) {
        Map<String, String> parrams = new HashMap<>();

        parrams.put(REQUEST_TITLE, request.getTitle());
        parrams.put(REQUEST_DESCRIPTION, request.getDescription());
        parrams.put(REQUEST_REQUEST_DETAILS, request.getDevices().toString());

        return mFDMSApi.updateRequest(request.getId(), parrams);
    }

    @Override
    public Observable<Request> getRequest(int requetsId) {
        return mFDMSApi.getRequest(requetsId)
            .flatMap(new Function<Respone<Request>, ObservableSource<Request>>() {
                @Override
                public ObservableSource<Request> apply(Respone<Request> requestRespone)
                    throws Exception {
                    return Utils.getResponse(requestRespone);
                }
            });
    }

    @Override
    public Observable<Request> registerAssignment(AssignmentRequest request) {
        Map<String, String> parrams = new HashMap<>();
        if (request.getRequestId() > 0) {
            parrams.put(ASSIGNMENT_REQUEST_ID, String.valueOf(request.getRequestId()));
        }

        if (request.getAssigneeId() > 0) {
            parrams.put(ASSIGNMENT_ASSIGNEE_ID, String.valueOf(request.getAssigneeId()));
        }

        if (request.getDescription() != null) {
            parrams.put(ASSIGNMENT_DESCRIPTION, request.getDescription());
        }

        if (request.getItemRequests() != null && request.getItemRequests().size() > 0) {
            parrams.put(ASSIGNMENT_ASSIGNMENT_DETAILS, request.getItemRequests().toString());
        }

        return mFDMSApi.registerAssignment(parrams)
            .flatMap(new Function<Respone<Request>, ObservableSource<Request>>() {
                @Override
                public ObservableSource<Request> apply(@NonNull Respone<Request> requestRespone)
                    throws Exception {
                    return Utils.getResponse(requestRespone);
                }
            });
    }

    @Override
    public Observable<String> registerAssignment(int staffId, List<AssignmentItemRequest> items) {
        Map<String, String> parrams = new HashMap<>();

        parrams.put(ASSIGNMENT_STAFF_ID, String.valueOf(staffId));
        if (items != null && items.size() > 0) {
            for (AssignmentItemRequest item : items) {
                parrams.put(ASSIGNMENT_ASSIGNMENT_DEVICE_ID, String.valueOf(item.getDeviceId()));
            }
        }

        return mFDMSApi.registerAssignmentForStaff(parrams)
            .flatMap(new Function<Respone<String>, ObservableSource<String>>() {
                @Override
                public ObservableSource<String> apply(@NonNull Respone<String> requestRespone)
                    throws Exception {
                    return Utils.getMesssage(requestRespone);
                }
            });
    }

    @Override
    public Observable<List<Dashboard>> getDashboardRequest() {
        return mFDMSApi.getDashboardRequest()
            .flatMap(new Function<Respone<List<Dashboard>>, ObservableSource<List<Dashboard>>>() {
                @Override
                public ObservableSource<List<Dashboard>> apply(Respone<List<Dashboard>> listRespone)
                    throws Exception {
                    return Utils.getResponse(listRespone);
                }
            });
    }

    private Map<String, Integer> getRequestParams(int requestType, int requestStatusId,
        int relativeId, int page, int perPage) {
        Map<String, Integer> parrams = new HashMap<>();
        if (requestStatusId != ALL_REQUEST_STATUS_ID) {
            parrams.put(REQUEST_STATUS_ID, requestStatusId);
        }
        if (relativeId != ALL_RELATIVE_ID) {
            parrams.put(RELATIVE_ID, relativeId);
        }
        if (perPage != OUT_OF_INDEX) {
            parrams.put(PER_PAGE, perPage);
        }
        if (page != OUT_OF_INDEX) {
            parrams.put(PAGE, page);
        }
        if (requestType != MY_REQUEST) {
            parrams.put(REQUEST_TYPE, requestType);
        }
        return parrams;
    }
}
