package com.framgia.fdms.screen.devicehistory;

import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.source.DeviceHistoryRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link DeviceHistoryFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
final class DeviceHistoryPresenter implements DeviceHistoryContract.Presenter {
    private static final String TAG = DeviceHistoryPresenter.class.getName();
    private final DeviceHistoryContract.ViewModel mViewModel;
    private DeviceHistoryRepository mDeviceHistoryRepository =
        DeviceHistoryRepository.getInstances();
    private CompositeDisposable mSubscription;

    public DeviceHistoryPresenter(DeviceHistoryContract.ViewModel viewModel) {
        mViewModel = viewModel;
        mSubscription = new CompositeDisposable();
    }

    @Override
    public void onStart() {
        getDeviceUsingHistory();
    }

    @Override
    public void onStop() {
    }

    @Override
    public void getDeviceUsingHistory() {
        Disposable subscription = mDeviceHistoryRepository.getListDeviceHistory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<DeviceUsingHistory>>() {
                @Override
                public void accept(List<DeviceUsingHistory> deviceUsingHistories) throws Exception {
                    mViewModel.onLoadDeviceHistorySuccess(deviceUsingHistories);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onLoadDeviceHistoryFailed();
                }
            });

        mSubscription.add(subscription);
    }
}
