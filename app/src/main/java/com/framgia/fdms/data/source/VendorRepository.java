package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by framgia on 03/07/2017.
 */
public class VendorRepository implements VendorDataSource.RemoteDataSource {
    private static VendorRepository sInstances;
    private VendorDataSource.RemoteDataSource mVendorRemoteDataSource;

    public VendorRepository(VendorDataSource.RemoteDataSource vendorRemoteDataSource) {
        mVendorRemoteDataSource = vendorRemoteDataSource;
    }

    public static VendorRepository getInstances(
        VendorDataSource.RemoteDataSource vendorRemoteDataSource) {
        if (sInstances == null) {
            sInstances = new VendorRepository(vendorRemoteDataSource);
        }
        return sInstances;
    }

    @Override
    public Observable<List<Producer>> getListVendor(String name, int page, int perPage) {
        return mVendorRemoteDataSource.getListVendor(name, page, perPage);
    }

    @Override
    public Observable<Producer> addVendor(Producer vendor) {
        return mVendorRemoteDataSource.addVendor(vendor);
    }

    @Override
    public Observable<Respone<String>> deleteVendor(Producer vendor) {
        return mVendorRemoteDataSource.deleteVendor(vendor);
    }

    @Override
    public Observable<String> editVendor(Producer vendor) {
        return mVendorRemoteDataSource.editVendor(vendor);
    }
}
