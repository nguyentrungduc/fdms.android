package com.framgia.fdms.screen.devicedetail.usinghistory;

import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.model.NewDeviceUsingHistory;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Listens to user actions from the UI ({@link DeviceUsingHistoryFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
final class DeviceUsingHistoryPresenter implements DeviceUsingHistoryContract.Presenter {

    private final DeviceUsingHistoryContract.ViewModel mViewModel;
    private DeviceRepository mRepository;
    private CompositeDisposable mCompositeSubscription;
    private int mDeviceId;

    public DeviceUsingHistoryPresenter(DeviceUsingHistoryContract.ViewModel viewModel,
        DeviceRepository repository, int deviceId) {
        mViewModel = viewModel;
        mCompositeSubscription = new CompositeDisposable();
        mRepository = repository;
        mDeviceId = deviceId;
        getUsingHistoryDevice();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mCompositeSubscription.clear();
    }

    @Override
    public void getUsingHistoryDevice() {
        mViewModel.showProgressbar();
        Disposable disposable = mRepository.getDeviceUsingHistories(mDeviceId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<NewDeviceUsingHistory>>() {
                @Override
                public void accept(List<NewDeviceUsingHistory> deviceUsingHistories)
                    throws Exception {
                    mViewModel.onGetUsingHistoryDeviceSuccess(deviceUsingHistories);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetUsingHistoryDeviceFailed(error.getMessage());
                    mViewModel.hideProgressbar();
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgressbar();
                }
            });

        mCompositeSubscription.add(disposable);
    }

    @Override
    public void onLoadMore() {
        // no ops
        mViewModel.hideProgressbar();
    }

    @Override
    public void getData(Status relative, Status status, Status assignee) {
        // no ops
    }
}
