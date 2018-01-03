package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Notification;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by Nhahv0902 on 6/1/2017.
 * <></>
 */

public interface NotificationDataSource {
    Observable<List<Notification>> getNotifications(int page, int perPage);

    Observable<String> markNoficationAsRead(int notifcationId);

    Observable<String> markAllNoficationsAsRead();

    Observable<Integer> getUnreadNotification();


}
