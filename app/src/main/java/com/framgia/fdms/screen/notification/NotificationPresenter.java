package com.framgia.fdms.screen.notification;

import com.framgia.fdms.data.model.Notification;
import com.framgia.fdms.data.source.NotificationRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Listens to user actions from the UI ({@link NotificationActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
public final class NotificationPresenter implements NotificationContract.Presenter {
    private static final String TAG = NotificationPresenter.class.getName();

    private final NotificationContract.ViewModel mViewModel;
    private NotificationRepository mRepository;
    private CompositeDisposable mSubscription;
    private int mPage = FIRST_PAGE;

    public NotificationPresenter(NotificationContract.ViewModel viewModel) {
        mViewModel = viewModel;
        mSubscription = new CompositeDisposable();
        mRepository = NotificationRepository.getInstances(FDMSServiceClient.getInstance());
        getNotifications();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    @Override
    public void getNotifications() {
        Disposable subscription = mRepository.getNotifications(mPage, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Notification>>() {
                    @Override
                    public void accept(List<Notification> notifications) throws Exception {
                        mViewModel.onLoadNotificationSuccess(notifications);
                        mViewModel.setAllowLoadMore(notifications != null
                                && notifications.size() == PER_PAGE);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mPage--;
                        mViewModel.onLoadNotificationFails(error.getMessage());
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void loadMoreNotification() {
        mPage++;
        getNotifications();
    }

    @Override
    public void refreshData() {
        mPage = FIRST_PAGE;
        getNotifications();
    }

    @Override
    public void markAllNotificaionAsRead() {
        Disposable subscription = mRepository.markAllNoficationsAsRead()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        mViewModel.onMarkAllNotificationAsReadSuccessfull(result);
                        mPage = FIRST_PAGE;
                        getNotifications();
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onMarkAllNotificationAsReadFailed(error.getMessage());
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void markNotificaionAsRead(final Notification notification) {
        if (notification == null) {
            return;
        }
        Disposable subscription = mRepository.markNoficationAsRead(notification.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        notification.setChecked(false);
                    }
                });
        mSubscription.add(subscription);
    }

}
