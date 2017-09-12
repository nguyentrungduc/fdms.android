package com.framgia.fdms.screen.devicedetail.history;

import com.framgia.fdms.data.model.DeviceHistoryDetail;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link DeviceDetailHistoryFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
public final class DeviceDetailHistoryPresenter implements DeviceDetailHistoryContract.Presenter {
    private static final String TAG = DeviceDetailHistoryPresenter.class.getName();

    private final DeviceDetailHistoryContract.ViewModel mViewModel;
    private DeviceRepository mRepository;
    private int mDeviceId = -1;

    public DeviceDetailHistoryPresenter(DeviceDetailHistoryContract.ViewModel viewModel,
        DeviceRepository repository, int deviceId) {
        mViewModel = viewModel;
        mRepository = repository;
        mDeviceId = deviceId;
    }

    @Override
    public void onStart() {
        getDetailHistory(mDeviceId);
    }

    @Override
    public void onStop() {
    }

    @Override
    public void getDetailHistory(int deviceId) {
        mRepository.getDeviceDetailHistory(deviceId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<DeviceHistoryDetail>>() {
                @Override
                public void accept(List<DeviceHistoryDetail> deviceHistoryDetails)
                    throws Exception {
                    mViewModel.onGetDeviceHistorySuccess(deviceHistoryDetails);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDeviceHistoryFailed(error.getMessage());
                }
            });
    }
}
