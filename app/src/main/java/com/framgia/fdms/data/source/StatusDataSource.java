package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.AssigneeUser;
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

        Observable<List<Status>> getListStatusRequest(String query);

        Observable<List<Status>> getListStatusEditRequest(int requestStatusId,String query);

        Observable<List<AssigneeUser>> getListRelative(String query, int page, int perPage);

        Observable<List<Status>> getListAssignee();

        Observable<List<Status>> getListUserBorrow(String name, int page, int perPage);
    }
}
