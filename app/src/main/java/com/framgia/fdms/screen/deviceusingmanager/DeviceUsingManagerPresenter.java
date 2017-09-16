package com.framgia.fdms.screen.deviceusingmanager;

import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.source.DeviceUsingHistoryDataSource;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link DeviceUsingManagerFragment}),
 * retrieves the data and updates
 * the UI as required.
 */
final class DeviceUsingManagerPresenter implements DeviceUsingManagerContract.Presenter {

    private final DeviceUsingManagerContract.ViewModel mViewModel;
    private DeviceUsingHistoryDataSource.RemoteDataSource mRepository;
    private CompositeDisposable mCompositeDisposable;

    DeviceUsingManagerPresenter(DeviceUsingManagerContract.ViewModel viewModel,
        DeviceUsingHistoryDataSource.RemoteDataSource repository) {
        mViewModel = viewModel;
        mRepository = repository;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onStart() {
        getDeviceUsingHistory();
    }

    @Override
    public void onStop() {
        mCompositeDisposable.clear();
    }

    @Override
    public void getDeviceUsingHistory() {
        Disposable disposable = mRepository.getListDeviceHistory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<DeviceUsingHistory>>() {
                @Override
                public void accept(List<DeviceUsingHistory> deviceUsingHistories) throws Exception {
                    mViewModel.onGetDeviceUsingHistorySuccess(deviceUsingHistories);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDeviceUsingHistoryFailed(error.getMessage());
                }
            });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void loadMoreData() {
        //// TODO: 9/16/2017
    }
}
