package com.framgia.fdms.screen.devicedetail;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.UserRepository;
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
    private UserRepository mUserRepository;
    private Device mDevice;

    public DeviceDetailPresenter(DeviceDetailContract.ViewModel viewModel,
        DeviceRepository repository, UserRepository userRepository, Device device) {
        mViewModel = viewModel;
        mSubscription = new CompositeDisposable();
        mRepository = repository;
        mUserRepository = userRepository;
        mDevice = device;
        getDevice(mDevice);
    }

    @Override
    public void onStart() {
        getCurrentUser();
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    @Override
    public void getDevice(final Device localDevice) {
        if (localDevice.getDeviceId() < 1) {
            return;
        }
        Disposable subscription = mRepository.getDevice(localDevice.getDeviceId())
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
                    mViewModel.onGetDeviceError(error.getMessage());
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void getCurrentUser() {
        Disposable subscription = mUserRepository.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<User>() {
                @Override
                public void accept(User user) throws Exception {
                    mViewModel.onGetUserSuccess(user);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetUserError(error.getMessage());
                }
            });
        mSubscription.add(subscription);
    }
}
