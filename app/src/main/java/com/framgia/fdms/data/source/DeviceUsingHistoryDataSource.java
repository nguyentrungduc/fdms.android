package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.screen.deviceusingmanager.DeviceUsingHistoryFilter;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by framgia on 14/07/2017.
 */
public class DeviceUsingHistoryDataSource {
    public interface RemoteDataSource {
        Observable<List<DeviceUsingHistory>> getListDeviceHistory(DeviceUsingHistoryFilter filter,
            int page, int perPage);

        Observable<List<Status>> getUsingStatuses();
    }
}
