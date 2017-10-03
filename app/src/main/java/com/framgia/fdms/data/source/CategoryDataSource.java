package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by MyPC on 05/05/2017.
 */

public class CategoryDataSource {
    public interface LocalDataSource {
    }

    public interface RemoteDataSource {
        Observable<List<Producer>> getListCategory(String query, int deviceGroupId, int page,
            int perPage);

        Observable<Producer> addDeviceCategory(Producer deviceCategory, int deviceGroupId);

        Observable<String> editDeviceCategory(Producer deviceCategory, int deviceGroupId);

        Observable<Respone<String>> deleteDeviceCategory(Producer deviceCategory);
    }
}
