package com.framgia.fdms.data.source.remote;

import android.text.TextUtils;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.source.CategoryDataSource;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.framgia.fdms.utils.Constant.ApiParram.DEVICE_CATEGORY_DESCRIPTION_MANAGER;
import static com.framgia.fdms.utils.Constant.ApiParram.DEVICE_CATEGORY_GROUP_ID_MANAGER;
import static com.framgia.fdms.utils.Constant.ApiParram.DEVICE_CATEGORY_NAME;
import static com.framgia.fdms.utils.Constant.ApiParram.DEVICE_CATEGORY_NAME_MANAGER;
import static com.framgia.fdms.utils.Constant.ApiParram.DEVICE_GROUP_ID;
import static com.framgia.fdms.utils.Constant.ApiParram.PAGE;
import static com.framgia.fdms.utils.Constant.ApiParram.PER_PAGE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * Created by MyPC on 05/05/2017.
 */

public class CategoryRemoteDataSource extends BaseRemoteDataSource
    implements CategoryDataSource.RemoteDataSource {
    private static final CategoryRemoteDataSource sInstance = new CategoryRemoteDataSource();

    public CategoryRemoteDataSource(FDMSApi FDMSApi) {
        super(FDMSApi);
    }

    public CategoryRemoteDataSource() {
        super(FDMSServiceClient.getInstance());
    }

    public static CategoryRemoteDataSource getInstance() {
        return sInstance;
    }

    @Override
    public Observable<List<Producer>> getListCategory(final String query, int deviceGroupId,
        int page, int perPage) {
        return mFDMSApi.getCategoriesByDeviceGroupId(
            getCategoryParams(query, deviceGroupId, page, perPage)).flatMap(

            new Function<Respone<List<Producer>>, ObservableSource<List<Producer>>>() {
                @Override
                public ObservableSource<List<Producer>> apply(
                    @NonNull Respone<List<Producer>> listRespone) throws Exception {
                    return Utils.getResponse(listRespone);
                }
            });
    }

    @Override
    public Observable<Producer> addDeviceCategory(Producer deviceCategory, int deviceGroupId) {
        return mFDMSApi.addDeviceCategory(
            setCategoryParams(deviceCategory.getName(), deviceCategory.getDescription(),
                String.valueOf(deviceGroupId)))
            .flatMap(new Function<Respone<Producer>, ObservableSource<Producer>>() {
                @Override
                public ObservableSource<Producer> apply(@NonNull Respone<Producer> producerRespone)
                    throws Exception {
                    return Utils.getResponse(producerRespone);
                }
            });
    }

    @Override
    public Observable<String> editDeviceCategory(Producer deviceCategory, int deviceGroupId) {
        return mFDMSApi.updateDeviceCategory(deviceCategory.getId(),
            setCategoryParams(deviceCategory.getName(), deviceCategory.getDescription(),
                String.valueOf(deviceGroupId)))
            .flatMap(new Function<Respone<String>, ObservableSource<String>>() {
                @Override
                public ObservableSource<String> apply(@NonNull Respone<String> stringRespone)
                    throws Exception {
                    return Utils.getResponse(stringRespone);
                }
            });
    }

    @Override
    public Observable<Respone<String>> deleteDeviceCategory(Producer deviceCategory) {
        return mFDMSApi.deleteDeviceCategory(deviceCategory.getId());
    }

    private Map<String, String> getCategoryParams(final String query, int deviceGroupId, int page,
        int perPage) {
        Map<String, String> parrams = new HashMap<>();
        if (!TextUtils.isEmpty(query)) {
            parrams.put(DEVICE_CATEGORY_NAME, query);
        }
        if (deviceGroupId != OUT_OF_INDEX) {
            parrams.put(DEVICE_GROUP_ID, String.valueOf(deviceGroupId));
        }
        if (page != OUT_OF_INDEX) {
            parrams.put(PAGE, String.valueOf(page));
        }
        if (perPage != OUT_OF_INDEX) {
            parrams.put(PER_PAGE, String.valueOf(perPage));
        }
        return parrams;
    }

    private Map<String, String> setCategoryParams(final String nameCategory, String description,
        String groupId) {
        Map<String, String> params = new HashMap<>();
        params.put(DEVICE_CATEGORY_NAME_MANAGER, nameCategory);
        params.put(DEVICE_CATEGORY_DESCRIPTION_MANAGER, description);
        params.put(DEVICE_CATEGORY_GROUP_ID_MANAGER, groupId);
        return params;
    }
}
