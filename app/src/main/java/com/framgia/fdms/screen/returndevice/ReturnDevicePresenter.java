package com.framgia.fdms.screen.returndevice;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.DeviceReturnRepository;
import com.framgia.fdms.data.source.StatusRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link ReturnDeviceActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
class ReturnDevicePresenter implements ReturnDeviceContract.Presenter {
    private static final String TAG = ReturnDevicePresenter.class.getName();

    private final ReturnDeviceContract.ViewModel mViewModel;
    private CompositeDisposable mSubscription;
    private StatusRepository mRepository;
    private DeviceRepository mDeviceRepository;
    private DeviceReturnRepository mDeviceReturnRepository;

    ReturnDevicePresenter(ReturnDeviceContract.ViewModel viewModel, StatusRepository repository,
        DeviceReturnRepository deviceReturnRepository, DeviceRepository deviceRepository) {
        mViewModel = viewModel;
        mRepository = repository;
        mDeviceReturnRepository = deviceReturnRepository;
        mDeviceRepository = deviceRepository;
        mSubscription = new CompositeDisposable();
        getListAssign();
    }

    @Override
    public void getListAssign() {
        Disposable subscription = mDeviceReturnRepository.getBorrowers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgressbar();
                }
            })
            .subscribe(new Consumer<List<Status>>() {
                @Override
                public void accept(List<Status> statuses) throws Exception {
                    mViewModel.onGetAssignedSuccess(statuses);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.hideProgressbar();
                    mViewModel.onLoadError(error.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgressbar();
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void getDevicesOfBorrower(int userId) {
        Disposable subscription = mDeviceReturnRepository.getListDevicesOfBorrower(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgressbar();
                }
            })
            .subscribe(new Consumer<List<Device>>() {
                @Override
                public void accept(List<Device> devices) throws Exception {
                    mViewModel.onDeviceLoaded(devices);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onError(error.getMessage());
                    mViewModel.hideProgressbar();
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgressbar();
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void getDeviceByCode(String codeDevice, final boolean isUserOther) {
        Disposable subscription = mDeviceRepository.getDeviceByQrCode(codeDevice)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgressbar();
                }
            })
            .subscribe(new Consumer<Device>() {
                @Override
                public void accept(Device device) throws Exception {
                    if (isUserOther) {
                        mViewModel.onGetDeviceUserOtherSuccess(device);
                    } else {
                        mViewModel.onGetDeviceSuccess(device);
                    }
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.hideProgressbar();
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgressbar();
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void returnDevice(List<Integer> listDeviceId) {
        Disposable subscription = mDeviceReturnRepository.returnDevice(listDeviceId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgressbar();
                }
            })
            .subscribe(new Consumer<String>() {
                @Override
                public void accept(@NonNull String s) throws Exception {
                    mViewModel.onReturnDeviceSuccess(s);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onError(error.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgressbar();
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void onStart() {
        getListAssign();
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }
}
