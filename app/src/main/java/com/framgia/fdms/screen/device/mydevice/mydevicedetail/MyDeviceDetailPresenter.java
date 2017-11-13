package com.framgia.fdms.screen.device.mydevice.mydevicedetail;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import com.framgia.fdms.utils.Constant;

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
 * Listens to user actions from the UI ({@link MyDeviceDetailFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
final class MyDeviceDetailPresenter implements MyDeviceDetailContract.Presenter {
    private static final String TAG = MyDeviceDetailPresenter.class.getName();

    private final MyDeviceDetailContract.ViewModel mViewModel;
    private CompositeDisposable mCompositeDisposable;
    private DeviceRepository mDeviceRepository;
    private String mType;
    private int mPage = FIRST_PAGE;
    private String mUserEmail;

    public MyDeviceDetailPresenter(MyDeviceDetailContract.ViewModel viewModel,
                                   @MyDeviceType int type, String userEmail, DeviceRepository deviceRepository) {
        mViewModel = viewModel;
        mType = getType(type);
        mUserEmail = userEmail;
        mDeviceRepository = deviceRepository;
        mCompositeDisposable = new CompositeDisposable();
        getData();
    }

    private String getType(@MyDeviceType int type) {
        switch (type) {
            case MyDeviceType.ALL:
                return Constant.DeviceUsingStatus.ALL;
            case MyDeviceType.USING:
                return Constant.DeviceUsingStatus.USING;
            case MyDeviceType.RETURNED:
                return Constant.DeviceUsingStatus.RETURN;
            default:
                return null;
        }
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mCompositeDisposable.clear();
    }

    @Override
    public void getData() {
        Disposable disposable = mDeviceRepository.getUserDevice(mType, mUserEmail, mPage, PER_PAGE)
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
                        if (deviceUsingHistories == null || deviceUsingHistories.size() == 0) {
                            return;
                        }
                        mViewModel.onGetDeviceSuccess(deviceUsingHistories.get(0).getUsingDevices());
                        mViewModel.setAllowLoadMore(deviceUsingHistories.size() == PER_PAGE);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onGetDataFailure(error.getMessage());
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
                        mViewModel.showGetDeviceByIdProgress();
                    }
                })
                .subscribe(new Consumer<Device>() {
                    @Override
                    public void accept(Device device) throws Exception {
                        if (device == null) {
                            return;
                        }
                        mViewModel.onGetDeviceByIdSuccess(device);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onGetDeviceByIdFailure(error.getMessage());
                        mViewModel.hideGetDeviceByIdProgress();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mViewModel.hideGetDeviceByIdProgress();
                    }
                });

        mCompositeDisposable.add(disposable);
    }
}
