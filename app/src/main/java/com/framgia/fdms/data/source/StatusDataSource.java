package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Status;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by MyPC on 05/05/2017.
 */

public class StatusDataSource {
    public interface LocalDataSource {
    }

    public interface RemoteDataSource {
        Observable<List<Status>> getListStatus();

        Observable<List<Status>> getListStatus(String query);

        Observable<List<Status>> getListStatusRequest();

        Observable<List<Status>> getListRelative();

        Observable<List<Status>> getListAssignee();
    }
}
