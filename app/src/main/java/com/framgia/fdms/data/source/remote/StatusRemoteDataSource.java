package com.framgia.fdms.data.source.remote;

import android.content.Context;
import android.text.TextUtils;

import com.framgia.fdms.FDMSApplication;
import com.framgia.fdms.R;
import com.framgia.fdms.data.anotation.RequestStatus;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.StatusDataSource;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

import static com.framgia.fdms.utils.Constant.ApiParram.NAME;
import static com.framgia.fdms.utils.Constant.ApiParram.PAGE;
import static com.framgia.fdms.utils.Constant.ApiParram.PER_PAGE;
import static com.framgia.fdms.utils.Constant.ApiParram.USER_NAME;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * Created by MyPC on 05/05/2017.
 */

public class StatusRemoteDataSource extends BaseRemoteDataSource
        implements StatusDataSource.RemoteDataSource {

    public StatusRemoteDataSource(FDMSApi FDMSApi) {
        super(FDMSApi);
    }

    @Override
    public Observable<List<Status>> getListStatus() {
        return mFDMSApi.getListStatus()
                .flatMap(new Function<Respone<List<Status>>, ObservableSource<List<Status>>>() {
                    @Override
                    public ObservableSource<List<Status>> apply(Respone<List<Status>> listRespone)
                            throws Exception {
                        return Utils.getResponse(listRespone);
                    }
                });
    }

    @Override
    public Observable<List<Status>> getListStatus(final String query) {
        if (TextUtils.isEmpty(query)) {
            return getListStatus();
        }
        return getListStatus().flatMap(
                new Function<List<Status>, ObservableSource<List<Status>>>() {
                    @Override
                    public ObservableSource<List<Status>> apply(List<Status> statuses)
                            throws Exception {
                        List<Status> data = new ArrayList<>();
                        for (Status status : statuses) {
                            if (status.getName()
                                    .toLowerCase(Locale.getDefault())
                                    .contains(query.toLowerCase(Locale.getDefault()))) {
                                data.add(status);
                            }
                        }
                        return Observable.just(data);
                    }
                });
    }

    @Override
    public Observable<List<Status>> getListStatusRequest() {
        return mFDMSApi.getListStatusRequest()
                .flatMap(new Function<Respone<List<Status>>, ObservableSource<List<Status>>>() {
                    @Override
                    public ObservableSource<List<Status>> apply(Respone<List<Status>> listRespone)
                            throws Exception {
                        return Utils.getResponse(listRespone);
                    }
                });
    }

    @Override
    public Observable<List<Status>> getListStatusRequest(final String query) {
        if (TextUtils.isEmpty(query)) {
            return getListStatusRequest();
        }
        return getListStatusRequest().flatMap(
                new Function<List<Status>, ObservableSource<List<Status>>>() {
                    @Override
                    public ObservableSource<List<Status>> apply(List<Status> statuses)
                            throws Exception {
                        List<Status> data = new ArrayList<>();
                        for (Status status : statuses) {
                            if (status.getName()
                                    .toLowerCase(Locale.getDefault())
                                    .contains(query.toLowerCase(Locale.getDefault()))) {
                                data.add(status);
                            }
                        }
                        return Observable.just(data);
                    }
                });
    }

    @Override
    public Observable<List<Status>> getListStatusEditRequest(int requestStatusId, String query) {
        List<Status> statuses = new ArrayList<>();
        Context context = FDMSApplication.getInstant();
        switch (requestStatusId) {
            case RequestStatus.CANCELLED:
            case RequestStatus.WAITING_DONE:
                statuses.add(new Producer(RequestStatus.CANCELLED,
                        context.getString(R.string.title_status_cancel)));
                statuses.add(new Producer(RequestStatus.WAITING_DONE,
                        context.getString(R.string.title_waiting_done)));
                break;
            case RequestStatus.APPROVED:
                statuses.add(new Producer(RequestStatus.CANCELLED,
                        context.getString(R.string.title_status_cancel)));
                statuses.add(new Producer(RequestStatus.APPROVED,
                        context.getString(R.string.title_approved)));
                statuses.add(new Producer(RequestStatus.WAITING_DONE,
                        context.getString(R.string.title_waiting_done)));
                break;
            default:
                break;
        }
        List<Status> statusesSearch = new ArrayList<>();
        for (Status status : statuses) {
            if (status.getName()
                    .toLowerCase(Locale.getDefault())
                    .contains(query.toLowerCase(Locale.getDefault()))) {
                statusesSearch.add(status);
            }
        }
        return Observable.just(statusesSearch);
    }

    @Override
    public Observable<List<Status>> getListRelative(final String query, int page, int perPage) {
        return mFDMSApi.getListRelative(getParams(query, page, perPage))
                .flatMap(new Function<Respone<List<Status>>, ObservableSource<List<Status>>>() {
                    @Override
                    public ObservableSource<List<Status>> apply(
                            @NonNull Respone<List<Status>> listRespone) throws Exception {
                        return Utils.getResponse(listRespone);
                    }
                });
    }

    @Override
    public Observable<List<Status>> getListAssignee() {
        return mFDMSApi.getListAssign()
                .flatMap(new Function<Respone<List<Status>>, ObservableSource<List<Status>>>() {
                    @Override
                    public ObservableSource<List<Status>> apply(Respone<List<Status>> listRespone)
                            throws Exception {
                        return Utils.getResponse(listRespone);
                    }
                });
    }

    @Override
    public Observable<List<Status>> getListUserBorrow(final String query, int page, int perPage) {
        return mFDMSApi.getListUserBorrow(getParamsUserBorrow(query, page, perPage))
                .flatMap(new Function<Respone<List<Status>>, ObservableSource<List<Status>>>() {
                    @Override
                    public ObservableSource<List<Status>> apply(
                            @NonNull Respone<List<Status>> listRespone) throws Exception {
                        return Utils.getResponse(listRespone);
                    }
                });
    }

    private Map<String, String> getParams(final String query, int page, int perPage) {
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(query)) {
            params.put(NAME, query);
        }
        if (page != OUT_OF_INDEX) {
            params.put(PAGE, String.valueOf(page));
        }
        if (perPage != OUT_OF_INDEX) {
            params.put(PER_PAGE, String.valueOf(perPage));
        }
        return params;
    }

    private Map<String, String> getParamsUserBorrow(final String query, int page, int perPage) {
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(query)) {
            params.put(USER_NAME, query);
        }
        if (page != OUT_OF_INDEX) {
            params.put(PAGE, String.valueOf(page));
        }
        if (perPage != OUT_OF_INDEX) {
            params.put(PER_PAGE, String.valueOf(perPage));
        }
        return params;
    }
}
