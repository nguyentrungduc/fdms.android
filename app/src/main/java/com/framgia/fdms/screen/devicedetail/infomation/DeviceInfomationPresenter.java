package com.framgia.fdms.screen.devicedetail.infomation;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.source.DeviceRepository;
import io.reactivex.disposables.CompositeDisposable;

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
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }
}
