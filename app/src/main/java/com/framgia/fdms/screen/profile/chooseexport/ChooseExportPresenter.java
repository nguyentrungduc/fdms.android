package com.framgia.fdms.screen.profile.chooseexport;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.source.DeviceReturnRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Created by tuanbg on 6/15/17.
 */

public class ChooseExportPresenter implements ChooseExportContract.Presenter {
    private ChooseExportContract.ViewModel mViewModel;
    private DeviceReturnRepository mDeviceRepository;
    private CompositeDisposable mCompositeSubscriptions;

    public ChooseExportPresenter(ChooseExportContract.ViewModel viewModel,
        DeviceReturnRepository deviceRepository) {
        mViewModel = viewModel;
        mDeviceRepository = deviceRepository;
        mCompositeSubscriptions = new CompositeDisposable();
        getListDevice();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        mCompositeSubscriptions.clear();
    }

    @Override
    public void getListDevice() {
        Disposable subscription = mDeviceRepository.devicesOfBorrower()
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
                    mViewModel.hideProgressbar();
                    mViewModel.onError(error.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgressbar();
                }
            });
        mCompositeSubscriptions.add(subscription);
    }
}
