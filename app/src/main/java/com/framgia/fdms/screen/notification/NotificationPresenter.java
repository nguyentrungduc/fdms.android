package com.framgia.fdms.screen.notification;

import com.framgia.fdms.data.model.Notification;
import com.framgia.fdms.data.source.NotificationRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link NotificationActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
public final class NotificationPresenter implements NotificationContract.Presenter {
    private static final String TAG = NotificationPresenter.class.getName();

    private final NotificationContract.ViewModel mViewModel;
    private NotificationRepository mRepository = NotificationRepository.getInstances();
    private CompositeDisposable mSubscription;

    public NotificationPresenter(NotificationContract.ViewModel viewModel) {
        mViewModel = viewModel;
        mSubscription = new CompositeDisposable();
    }

    @Override
    public void onStart() {
        getNotifications();
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    @Override
    public void getNotifications() {
        Disposable subscription = mRepository.getNotifications()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<Notification>>() {
                @Override
                public void accept(List<Notification> notifications) throws Exception {
                    mViewModel.onLoadNotificationSuccess(notifications);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onLoadNotificationFails(error.getMessage());
                }
            });
        mSubscription.add(subscription);
    }
}
