package com.framgia.fdms.data.source.remote;

import com.framgia.fdms.data.model.Notification;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.source.NotificationDataSource;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.utils.Utils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nhahv0902 on 6/1/2017.
 * <></>
 */

public class NotificationRemoteDataSource extends BaseRemoteDataSource implements
        NotificationDataSource {

    private static NotificationRemoteDataSource sInstances;

    public NotificationRemoteDataSource(FDMSApi fdmsApi) {
        super(fdmsApi);
    }


    public static NotificationRemoteDataSource getInstances(FDMSApi fdmsApi) {
        if (sInstances == null) {
            sInstances = new NotificationRemoteDataSource(fdmsApi);
        }
        return sInstances;
    }

    @Override
    public Observable<List<Notification>> getNotifications(int page, int perPage) {
        return mFDMSApi.getNotifications(page, perPage)
                .flatMap(new Function<Respone<List<Notification>>,
                        ObservableSource<List<Notification>>>() {
                    @Override
                    public ObservableSource<List<Notification>> apply(
                            Respone<List<Notification>> listRespone) throws Exception {
                        return Utils.getResponse(listRespone);
                    }
                });
    }

    @Override
    public Observable<String> markNoficationAsRead(int notifcationId) {
        // TODO: 1/3/18 implement api
        return Observable.just("markNoficationAsRead "+notifcationId +" successfully");
    }

    @Override
    public Observable<String> markAllNoficationsAsRead() {
        // TODO: 1/3/18 implement api
        return Observable.just("markAllNoficationsAsRead successfully");
    }
}
