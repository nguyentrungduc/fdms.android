package com.framgia.fdms.data.source.remote;

import android.text.TextUtils;

import com.framgia.fdms.data.model.AssignmentRequest;
import com.framgia.fdms.data.model.Dashboard;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.RequestDataSource;
import com.framgia.fdms.data.source.api.request.RequestCreatorRequest;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

import static com.framgia.fdms.screen.requestcreation.member.RequestCreatorType.MEMBER_REQUEST;
import static com.framgia.fdms.screen.requestcreation.member.RequestCreatorType.MY_REQUEST;
import static com.framgia.fdms.utils.Constant.ALL_RELATIVE_ID;
import static com.framgia.fdms.utils.Constant.ALL_REQUEST_STATUS_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.PAGE;
import static com.framgia.fdms.utils.Constant.ApiParram.PER_PAGE;
import static com.framgia.fdms.utils.Constant.ApiParram.RELATIVE_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_ASSIGNEE_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_DESCRIPTION;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_FOR_USER_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_GROUP_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_REQUEST_DETAILS;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_STATUS_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_STATUS_ID_EDIT;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_TITLE;
import static com.framgia.fdms.utils.Constant.ApiParram.REQUEST_TYPE;
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
    public Observable<List<Request>> getMemberRequests(int requestStatusId,
                                                       int relativeId, int page, int perPage) {
        return mFDMSApi.getRequests(
                getRequestParams(MEMBER_REQUEST, requestStatusId, relativeId, page, perPage))
                .flatMap(new Function<Respone<List<Request>>, ObservableSource<List<Request>>>() {
                    @Override
                    public ObservableSource<List<Request>> apply(Respone<List<Request>> listRespone)
                            throws Exception {
                        return Utils.getResponse(listRespone);
                    }
                });
    }

    @Override
    public Observable<List<Request>> getMyRequests(int requestStatusId,
                                                   int relativeId, int page, int perPage) {
        return mFDMSApi.getRequests(
                getRequestParams(MY_REQUEST, requestStatusId, relativeId, page, perPage))
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
        if (request.getGroupId() > 0) {
            parrams.put(REQUEST_GROUP_ID, String.valueOf(request.getGroupId()));
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
        if (request.getRequestForId() > 0) {
            parrams.put(REQUEST_FOR_USER_ID, String.valueOf(request.getRequestForId()));
        }
        if (request.getAssigneeId() > 0) {
            parrams.put(REQUEST_ASSIGNEE_ID, String.valueOf(request.getAssigneeId()));
        }
        if (request.getRequestStatusId() > 0) {
            parrams.put(REQUEST_STATUS_ID_EDIT, String.valueOf(request.getRequestStatusId()));
        }
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
    public Observable<Request> assignDeviceForRequest(AssignmentRequest request) {
        List<Integer> values = new ArrayList<>();
        for (Device device : request.getDevices()) {
            values.add(device.getId());
        }

        return mFDMSApi.assignDeviceForRequest(request.getRequestId(),
                request.getAssigneeId(),
                request.getDescription(),
                values)
                .flatMap(new Function<Respone<Request>, ObservableSource<Request>>() {
                    @Override
                    public ObservableSource<Request> apply(@NonNull Respone<Request> requestRespone)
                            throws Exception {
                        return Utils.getResponse(requestRespone);
                    }
                });
    }

    @Override
    public Observable<String> assignDeviceForNewMember(int staffId, List<Device> items) {
        List<Integer> values = new ArrayList<>();
        for (Device device : items) {
            values.add(device.getId());
        }
        return mFDMSApi.assignDeviceForNewMember(staffId, values)
                .flatMap(new Function<Respone<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull Respone<String> requestRespone)
                            throws Exception {
                        return Utils.getMesssage(requestRespone);
                    }
                });
    }

    @Override
    public Observable<String> assignDeviceForMeetingRoom(int meetingRoomId, List<Device> items) {
        List<Integer> values = new ArrayList<>();
        for (Device device : items) {
            values.add(device.getId());
        }
        return mFDMSApi.assignDeviceForMeetingRoom(meetingRoomId, values)
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
