package com.framgia.fdms.screen.devicedetail.infomation;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Listens to user actions from the UI ({@link DeviceInfomationFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
final class DeviceInfomationPresenter implements DeviceInfomationContract.Presenter {

    private final DeviceInfomationContract.ViewModel mViewModel;
    private CompositeDisposable mSubscription;
    private DeviceRepository mRepository;
    private Device mDevice;

    public DeviceInfomationPresenter(DeviceInfomationContract.ViewModel viewModel,
        DeviceRepository repository, Device device) {
        mViewModel = viewModel;
        mRepository = repository;
        mDevice = device;
        mSubscription = new CompositeDisposable();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    @Override
    public void deleteDevice(final Device device) {
        Disposable subscription = mRepository.deleteDevice(device)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Respone<String>>() {
                @Override
                public void accept(Respone<String> respone) throws Exception {
                    if (!respone.isError()) {
                        mViewModel.onDeleteDeviceSuccess(device, respone.getMessage());
                    }
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onDeleteDeviceError(error.getMessage());
                }
            });
        mSubscription.add(subscription);
    }
}
