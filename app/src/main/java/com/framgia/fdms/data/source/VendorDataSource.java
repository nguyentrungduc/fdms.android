package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by framgia on 03/07/2017.
 */

public class VendorDataSource {
    public interface RemoteDataSource {
        Observable<List<Producer>> getListVendor(int page, int perPage);

        Observable<Producer> addVendor(Producer producer);

        Observable<Respone<String>> deleteVendor(Producer producer);

        Observable<String> editVendor(Producer producer);
    }
}
