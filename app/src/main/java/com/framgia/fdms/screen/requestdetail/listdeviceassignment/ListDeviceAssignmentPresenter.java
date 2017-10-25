package com.framgia.fdms.screen.requestdetail.listdeviceassignment;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Listens to user actions from the UI ({@link ListDeviceAssignmentFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
final class ListDeviceAssignmentPresenter implements ListDeviceAssignmentContract.Presenter {

    private final ListDeviceAssignmentContract.ViewModel mViewModel;
    private DeviceRepository mDeviceRepository;
    private CompositeDisposable mCompositeDisposable;

    ListDeviceAssignmentPresenter(ListDeviceAssignmentContract.ViewModel viewModel,
                                  DeviceRepository deviceRepository) {
        mViewModel = viewModel;
        mDeviceRepository = deviceRepository;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        mCompositeDisposable.clear();
    }

    @Override
    public void getDeviceByDeviceId(int deviceId) {
        Disposable disposable = mDeviceRepository.getDevice(deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Device>() {
                    @Override
                    public void accept(Device device) throws Exception {
                        mViewModel.onGetDeviceDetailSuccess(device);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onGetDeviceDetailFailure(error.getMessage());
                    }
                });

        mCompositeDisposable.add(disposable);
    }
}
