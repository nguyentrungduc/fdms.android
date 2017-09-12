package com.framgia.fdms.screen.devicedetail;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Listens to user actions from the UI ({@link DeviceDetailActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
final class DeviceDetailPresenter implements DeviceDetailContract.Presenter {

    private final DeviceDetailContract.ViewModel mViewModel;
    private CompositeDisposable mSubscription;
    private DeviceRepository mRepository;
    private Device mDevice;

    public DeviceDetailPresenter(DeviceDetailContract.ViewModel viewModel,
        DeviceRepository repository, Device device) {
        mViewModel = viewModel;
        mSubscription = new CompositeDisposable();
        mRepository = repository;
        mDevice = device;
        getDevice(mDevice);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    @Override
    public void getDevice(final Device localDevice) {
        Disposable subscription = mRepository.getDevice(localDevice.getId())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Consumer<Device>() {
                @Override
                public void accept(Device device) throws Exception {
                    localDevice.cloneDevice(device);
                    mViewModel.onGetDeviceSuccess(localDevice);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDeviceError();
                }
            });
        mSubscription.add(subscription);
    }
}
