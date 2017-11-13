package com.framgia.fdms.screen.deviceusingmanager;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.DeviceUsingHistoryDataSource;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Listens to user actions from the UI ({@link DeviceUsingManagerFragment}),
 * retrieves the data and updates
 * the UI as required.
 */
final class DeviceUsingManagerPresenter implements DeviceUsingManagerContract.Presenter {

    private final DeviceUsingManagerContract.ViewModel mViewModel;
    private DeviceUsingHistoryDataSource.RemoteDataSource mRepository;
    private CompositeDisposable mCompositeDisposable;
    private int mPage = FIRST_PAGE;
    private DeviceUsingHistoryFilter mFilter;
    private DeviceRepository mDeviceRepository;

    DeviceUsingManagerPresenter(DeviceUsingManagerContract.ViewModel viewModel,
                                DeviceUsingHistoryDataSource.RemoteDataSource repository,
                                DeviceRepository deviceRepository) {
        mViewModel = viewModel;
        mRepository = repository;
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
    public void getDeviceUsingHistory(DeviceUsingHistoryFilter filter) {
        mFilter = filter;
        getData();
    }

    private void getData() {
        Disposable disposable = mRepository.getListDeviceHistory(mFilter, mPage, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mViewModel.showProgress();
                    }
                })
                .subscribe(new Consumer<List<DeviceUsingHistory>>() {
                    @Override
                    public void accept(List<DeviceUsingHistory> deviceUsingHistories) throws Exception {
                        mViewModel.onGetDeviceUsingHistorySuccess(deviceUsingHistories);
                        mViewModel.setAllowLoadMore(
                                deviceUsingHistories != null && deviceUsingHistories.size() == PER_PAGE);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onGetDeviceUsingHistoryFailed(error.getMessage());
                        mViewModel.hideProgress();
                        mPage--;
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mViewModel.hideProgress();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void loadMoreData() {
        mPage++;
        getData();
    }

    @Override
    public void getDeviceByDeviceId(int deviceId) {
        Disposable disposable = mDeviceRepository.getDevice(deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mViewModel.showProgress();
                    }
                })
                .subscribe(new Consumer<Device>() {
                    @Override
                    public void accept(Device device) throws Exception {
                        mViewModel.onGetDeviceSuccess(device);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onGetDeviceFailure(error.getMessage());
                        mViewModel.hideProgress();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mViewModel.hideProgress();
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
