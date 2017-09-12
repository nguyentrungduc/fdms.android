package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Category;
import com.framgia.fdms.data.source.remote.CategoryRemoteDataSource;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by MyPC on 05/05/2017.
 */

public class CategoryRepository {
    private CategoryRemoteDataSource mCategoryRemoteDataSource;

    public CategoryRepository(CategoryRemoteDataSource categoryRemoteDataSource) {
        mCategoryRemoteDataSource = categoryRemoteDataSource;
    }

    public Observable<List<Category>> getListCategory() {
        return mCategoryRemoteDataSource.getListCategory();
    }
}
