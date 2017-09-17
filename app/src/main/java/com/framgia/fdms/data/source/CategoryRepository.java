package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.remote.CategoryRemoteDataSource;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by MyPC on 05/05/2017.
 */

public class CategoryRepository implements CategoryDataSource.RemoteDataSource {
    private CategoryRemoteDataSource mCategoryRemoteDataSource;

    public CategoryRepository(CategoryRemoteDataSource categoryRemoteDataSource) {
        mCategoryRemoteDataSource = categoryRemoteDataSource;
    }

    public Observable<List<Status>> getListCategory() {
        return mCategoryRemoteDataSource.getListCategory();
    }

    @Override
    public Observable<List<Status>> getListCategory(String query) {
        return mCategoryRemoteDataSource.getListCategory(query);
    }

    @Override
    public Observable<List<Status>> getListCategory(String query, int deviceGroupId) {
        return  mCategoryRemoteDataSource.getListCategory(query, deviceGroupId);
    }
}
