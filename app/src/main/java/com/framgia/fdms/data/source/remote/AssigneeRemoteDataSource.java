package com.framgia.fdms.data.source.remote;

import android.text.TextUtils;

import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.AssigneeDataSource;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.data.source.remote.BaseRemoteDataSource;
import com.framgia.fdms.utils.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by MyPC on 05/05/2017.
 */

public class AssigneeRemoteDataSource extends BaseRemoteDataSource implements AssigneeDataSource {
    private static AssigneeRemoteDataSource sInstance;

    public static AssigneeRemoteDataSource getInstance(FDMSApi api) {
        if (sInstance == null) {
            sInstance = new AssigneeRemoteDataSource(api);
        }
        return sInstance;
    }

    public AssigneeRemoteDataSource(FDMSApi api) {
        super(api);
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
    public Observable<List<Status>> getListAssignee(final String name) {
        if (TextUtils.isEmpty(name)) {
            return getListAssignee();
        }
        return getListAssignee()
                .flatMap(new Function<List<Status>, ObservableSource<List<Status>>>() {
                    @Override
                    public ObservableSource<List<Status>> apply(List<Status> statuses)
                            throws Exception {
                        List<Status> result = new ArrayList<>();
                        for (Status status : statuses) {
                            if (status.getName().toLowerCase().contains(name.toLowerCase())) {
                                result.add(status);
                            }
                        }
                        return Observable.just(result);
                    }
                });
    }
}
