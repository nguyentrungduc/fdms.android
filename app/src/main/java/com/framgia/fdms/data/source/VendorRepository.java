package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.source.remote.VendorRemoteDataSource;

import java.util.List;

import rx.Observable;

/**
 * Created by framgia on 03/07/2017.
 */
public class VendorRepository {
    private VendorRemoteDataSource mVendorRemoteDataSource;
    private static VendorRepository sInstances;

    public VendorRepository(VendorRemoteDataSource vendorRemoteDataSource) {
        mVendorRemoteDataSource = vendorRemoteDataSource;
    }

    private VendorRepository() {
        mVendorRemoteDataSource = VendorRemoteDataSource.getInstances();
    }

    public static VendorRepository getInstances() {
        if (sInstances == null) {
            sInstances = new VendorRepository();
        }
        return sInstances;
    }

    public Observable<List<Producer>> getListVendor() {
        return mVendorRemoteDataSource.getListVendor();
    }

    public Observable<Void> addVendor(Producer vendor) {
        return mVendorRemoteDataSource.addVendor(vendor);
    }

    public Observable<Void> deleteVendor(Producer vendor) {
        return mVendorRemoteDataSource.deleteVendor(vendor);
    }

    public Observable<Void> editVendor(Producer vendor) {
        return mVendorRemoteDataSource.editVendor(vendor);
    }
}
