package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by MyPC on 05/05/2017.
 */

public class CategoryDataSource {
    public interface LocalDataSource {
    }

    public interface RemoteDataSource {
        Observable<List<Producer>> getListCategory();

        Observable<List<Producer>> getListCategory(String query);

        Observable<List<Producer>> getListCategory(String query, int deviceGroupId);
    }
}
