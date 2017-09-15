package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Status;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by MyPC on 05/05/2017.
 */

public class CategoryDataSource {
    public interface LocalDataSource {
    }

    public interface RemoteDataSource {
        Observable<List<Status>> getListCategory();

        Observable<List<Status>> getListCategory(String query);

        Observable<List<Status>> getCategoriesByDeviceGroupId(int deviceGroupId);
    }
}
