package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Producer;
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

    public Observable<List<Producer>> getListCategory() {
        return mCategoryRemoteDataSource.getListCategory();
    }

    @Override
    public Observable<List<Producer>> getListCategory(String query) {
        return mCategoryRemoteDataSource.getListCategory(query);
    }

    @Override
    public Observable<List<Producer>> getListCategory(String query, int deviceGroupId) {
        return mCategoryRemoteDataSource.getListCategory(query, deviceGroupId);
    }
}
