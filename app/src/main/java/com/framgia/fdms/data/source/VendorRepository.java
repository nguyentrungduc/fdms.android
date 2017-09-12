package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import java.util.List;
import rx.Observable;

/**
 * Created by framgia on 03/07/2017.
 */
public class VendorRepository implements VendorDataSource.RemoteDataSource {
    private VendorDataSource.RemoteDataSource mVendorRemoteDataSource;
    private static VendorRepository sInstances;

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
    public Observable<List<Producer>> getListVendor(int page, int perPage) {
        return mVendorRemoteDataSource.getListVendor(page, perPage);
    }

    @Override
    public Observable<Producer> addVendor(Producer vendor) {
        return mVendorRemoteDataSource.addVendor(vendor);
    }

    @Override
    public Observable<String> deleteVendor(Producer vendor) {
        return mVendorRemoteDataSource.deleteVendor(vendor);
    }

    @Override
    public Observable<Void> editVendor(Producer vendor) {
        return mVendorRemoteDataSource.editVendor(vendor);
    }
}
