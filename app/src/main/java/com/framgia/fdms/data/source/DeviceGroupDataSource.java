package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by framgia on 9/13/17.
 */

public interface DeviceGroupDataSource {
    Observable<List<Producer>> getListDeviceGroup(String name);

    Observable<Producer> addDeviceGroup(Producer deviceGroup);

    Observable<Respone<String>> deleteDeviceGroup(Producer deviceGroup);

    Observable<String> editDeviceGroup(Producer deviceGroup);
}
