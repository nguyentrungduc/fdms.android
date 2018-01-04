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
    private static int UNREAD_NOTIFICATION = 101;

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
        return mFDMSApi.markNotificationAsRead(notifcationId)
                .flatMap(new Function<Respone, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Respone stringRespone)
                            throws Exception {
                        if (stringRespone != null) {
                            return Observable.just(stringRespone.getMessage());
                        }
                        return Observable.error(new Exception());
                    }
                });
    }

    @Override
    public Observable<String> markAllNoficationsAsRead() {
        return mFDMSApi.markAllNotificationsAsRead()
                .flatMap(new Function<Respone, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Respone stringRespone)
                            throws Exception {
                        if (stringRespone != null) {
                            return Observable.just(stringRespone.getMessage());
                        }
                        return Observable.error(new Exception());
                    }
                });
    }

    @Override
    public Observable<Integer> getUnreadNotification() {
        // TODO: 1/3/18 implement api later
        UNREAD_NOTIFICATION--;
        if (UNREAD_NOTIFICATION > 96) {
            return Observable.just(UNREAD_NOTIFICATION);
        } else {
            return Observable.just(0);
        }
    }
}
