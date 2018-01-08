package com.framgia.fdms.data.source.remote;

import com.framgia.fdms.data.model.Notification;
import com.framgia.fdms.data.model.NotificationResult;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.source.NotificationDataSource;
import com.framgia.fdms.data.source.api.service.FDMSApi;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.PER_PAGE;

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
                .flatMap(new Function<Respone<NotificationResult>,
                        ObservableSource<List<Notification>>>() {
                    @Override
                    public ObservableSource<List<Notification>> apply(
                            Respone<NotificationResult> result) throws Exception {
                        if (result == null) {
                            return Observable.error(new NullPointerException());
                        }
                        if (result.isError()) {
                            return Observable.error(
                                    new NullPointerException("ERROR" + result.getStatus()));
                        }
                        return Observable.just(result.getData().getNotifications());
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
        return mFDMSApi.getNotifications(FIRST_PAGE, PER_PAGE)
                .flatMap(new Function<Respone<NotificationResult>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> apply(
                            Respone<NotificationResult> result) throws Exception {
                        if (result == null) {
                            return Observable.error(new NullPointerException());
                        }
                        if (result.isError()) {
                            return Observable.error(
                                    new NullPointerException("ERROR" + result.getStatus()));
                        }
                        return Observable.just(result.getData().getCountUnreadMesssages());
                    }
                });
    }
}
