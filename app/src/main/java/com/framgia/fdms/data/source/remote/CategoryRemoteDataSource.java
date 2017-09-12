package com.framgia.fdms.data.source.remote;

import com.framgia.fdms.data.model.Category;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.source.CategoryDataSource;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import java.util.List;

/**
 * Created by MyPC on 05/05/2017.
 */

public class CategoryRemoteDataSource extends BaseRemoteDataSource
    implements CategoryDataSource.RemoteDataSource {
    public CategoryRemoteDataSource(FDMSApi FDMSApi) {
        super(FDMSApi);
    }

    @Override
    public Observable<List<Category>> getListCategory() {
        return mFDMSApi.getListCategory()
            .flatMap(new Function<Respone<List<Category>>, ObservableSource<List<Category>>>() {
                @Override
                public ObservableSource<List<Category>> apply(Respone<List<Category>> listRespone)
                    throws Exception {
                    return null;
                }
            });
    }
}
