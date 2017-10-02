package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by MyPC on 05/05/2017.
 */

public class CategoryRepository implements CategoryDataSource.RemoteDataSource {
    private static CategoryRepository sInstance;
    private CategoryDataSource.RemoteDataSource mCategoryRemoteDataSource;

    public static CategoryRepository getInstance(
        CategoryDataSource.RemoteDataSource categoryRemoteDataSource) {
        if (sInstance == null) {
            sInstance = new CategoryRepository(categoryRemoteDataSource);
        }
        return sInstance;
    }

    public CategoryRepository(CategoryDataSource.RemoteDataSource categoryRemoteDataSource) {
        mCategoryRemoteDataSource = categoryRemoteDataSource;
    }

    @Override
    public Observable<List<Producer>> getListCategory(String query, int deviceGroupId, int page,
        int perPage) {
        return mCategoryRemoteDataSource.getListCategory(query, deviceGroupId, page, perPage);
    }

    @Override
    public Observable<Producer> addDeviceCategory(Producer deviceCategory, int deviceGroupId) {
        return mCategoryRemoteDataSource.addDeviceCategory(deviceCategory, deviceGroupId);
    }

    @Override
    public Observable<String> editDeviceCategory(Producer deviceCategory, int deviceGroupId) {
        return mCategoryRemoteDataSource.editDeviceCategory(deviceCategory, deviceGroupId);
    }

    @Override
    public Observable<Respone<String>> deleteDeviceCategory(Producer deviceCategory) {
        return mCategoryRemoteDataSource.deleteDeviceCategory(deviceCategory);
    }
}
