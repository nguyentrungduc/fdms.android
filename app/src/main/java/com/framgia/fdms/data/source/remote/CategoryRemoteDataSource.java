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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.framgia.fdms.utils.Constant.ApiParram.DEVICE_GROUP_ID;
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
    public Observable<List<Producer>> getListCategory() {
        return mFDMSApi.getListCategory()
            .flatMap(new Function<Respone<List<Producer>>, ObservableSource<List<Producer>>>() {
                @Override
                public ObservableSource<List<Producer>> apply(Respone<List<Producer>> listRespone)
                    throws Exception {
                    return Utils.getResponse(listRespone);
                }
            });
    }

    @Override
    public Observable<List<Producer>> getListCategory(final String query) {
        if (TextUtils.isEmpty(query)) {
            return getListCategory();
        }
        return getListCategory().flatMap(
            new Function<List<Producer>, ObservableSource<List<Producer>>>() {
                @Override
                public ObservableSource<List<Producer>> apply(List<Producer> producers)
                    throws Exception {
                    List<Producer> data = new ArrayList<>();
                    for (Producer producer : producers) {
                        if (producer.getName()
                            .toLowerCase(Locale.getDefault())
                            .contains(query.toLowerCase(Locale.getDefault()))) {
                            data.add(producer);
                        }
                    }
                    return Observable.just(data);
                }
            });
    }

    public Observable<List<Producer>> getListCategory(int deviceGroupId) {
        Map<String, String> parrams = new HashMap();
        if (deviceGroupId != OUT_OF_INDEX) {
            parrams.put(DEVICE_GROUP_ID, String.valueOf(deviceGroupId));
        }
        return mFDMSApi.getCategoriesByDeviceGroupId(parrams)
            .flatMap(new Function<Respone<List<Producer>>, ObservableSource<List<Producer>>>() {
                @Override
                public ObservableSource<List<Producer>> apply(
                    @NonNull Respone<List<Producer>> listRespone) throws Exception {
                    return Utils.getResponse(listRespone);
                }
            });
    }

    @Override
    public Observable<List<Producer>> getListCategory(final String query, int deviceGroupId) {
        if (TextUtils.isEmpty(query)) {
            return getListCategory(deviceGroupId);
        }
        return getListCategory(deviceGroupId).flatMap(
            new Function<List<Producer>, ObservableSource<List<Producer>>>() {
                @Override
                public ObservableSource<List<Producer>> apply(@NonNull List<Producer> producers)
                    throws Exception {
                    List<Producer> data = new ArrayList<>();
                    for (Producer producer : producers) {
                        if (producer.getName()
                            .toLowerCase(Locale.getDefault())
                            .contains(query.toLowerCase(Locale.getDefault()))) {
                            data.add(producer);
                        }
                    }
                    return Observable.just(data);
                }
            });
    }
}
