package com.framgia.fdms.data.source.remote;

import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.StatusDataSource;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import java.util.List;

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
    public Observable<List<Status>> getListRelative() {
        return mFDMSApi.getListRelative()
            .flatMap(new Function<Respone<List<Status>>, ObservableSource<List<Status>>>() {
                @Override
                public ObservableSource<List<Status>> apply(Respone<List<Status>> listRespone)
                    throws Exception {
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
}
