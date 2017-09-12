package com.framgia.fdms.data.source.remote;

import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.BranchDataSource;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import java.util.List;

/**
 * Created by MyPC on 13/06/2017.
 */

public class BranchRemoteDataSource extends BaseRemoteDataSource implements BranchDataSource {
    public BranchRemoteDataSource(FDMSApi FDMSApi) {
        super(FDMSApi);
    }

    @Override
    public Observable<List<Status>> getListBranch() {
        return mFDMSApi.getListBranch()
            .flatMap(new Function<Respone<List<Status>>, ObservableSource<List<Status>>>() {
                @Override
                public ObservableSource<List<Status>> apply(Respone<List<Status>> listRespone)
                    throws Exception {
                    return Utils.getResponse(listRespone);
                }
            });
    }
}
