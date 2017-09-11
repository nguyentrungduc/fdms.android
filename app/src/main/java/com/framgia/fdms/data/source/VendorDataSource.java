package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import java.util.List;
import rx.Observable;

/**
 * Created by framgia on 03/07/2017.
 */

public class VendorDataSource {
    public interface RemoteDataSource {
        Observable<List<Producer>> getListVendor(int page, int perPage);

        Observable<Producer> addVendor(Producer producer);

        Observable<Void> deleteVendor(Producer producer);

        Observable<Void> editVendor(Producer producer);
    }
}
