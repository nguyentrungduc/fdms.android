package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Notification;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.framgia.fdms.data.source.remote.NotificationRemoteDataSource;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by Nhahv0902 on 6/1/2017.
 * <></>
 */

public class NotificationRepository implements NotificationDataSource {

    private static NotificationRepository sInstances;
    private NotificationRemoteDataSource mDataSource;

    private NotificationRepository(FDMSApi fdmsApi) {
        mDataSource = NotificationRemoteDataSource.getInstances(fdmsApi);
    }

    public static NotificationRepository getInstances(FDMSApi fdmsApi) {
        if (sInstances == null) {
            sInstances = new NotificationRepository(fdmsApi);
        }
        return sInstances;
    }

    @Override
    public Observable<List<Notification>> getNotifications(int page, int perPage) {
        return mDataSource.getNotifications(page, perPage);
    }

    @Override
    public Observable<String> markNoficationAsRead(int notifcationId) {
        return mDataSource.markNoficationAsRead(notifcationId);
    }

    @Override
    public Observable<String> markAllNoficationsAsRead() {
        return mDataSource.markAllNoficationsAsRead();
    }

    @Override
    public Observable<Integer> getUnreadNotification() {
        return mDataSource.getUnreadNotification();
    }
}
