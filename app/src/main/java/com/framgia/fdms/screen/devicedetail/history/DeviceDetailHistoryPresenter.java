package com.framgia.fdms.screen.devicedetail.history;

import com.framgia.fdms.data.model.DeviceHistoryDetail;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Listens to user actions from the UI ({@link DeviceDetailHistoryFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
public final class DeviceDetailHistoryPresenter implements DeviceDetailHistoryContract.Presenter {

    private final DeviceDetailHistoryContract.ViewModel mViewModel;
    private DeviceRepository mRepository;
    private int mDeviceId = -1;
    private int mPage = 1;
    private CompositeDisposable mCompositeDisposable;

    public DeviceDetailHistoryPresenter(DeviceDetailHistoryContract.ViewModel viewModel,
        DeviceRepository repository, int deviceId) {
        mViewModel = viewModel;
        mRepository = repository;
        mDeviceId = deviceId;
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
    public void getDetailHistory() {
        Disposable disposable = mRepository.getDeviceDetailHistory(mDeviceId, mPage, PER_PAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgress();
                }
            })
            .subscribe(new Consumer<List<DeviceHistoryDetail>>() {
                @Override
                public void accept(List<DeviceHistoryDetail> deviceHistoryDetails)
                    throws Exception {
                    mViewModel.onGetDeviceHistorySuccess(deviceHistoryDetails);
                    mViewModel.setAllowLoadMore(
                        deviceHistoryDetails != null && deviceHistoryDetails.size() == PER_PAGE);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDeviceHistoryFailed(error.getMessage());
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

    @Override
    public void loadMoreData() {
        mPage++;
        getDetailHistory();
    }
}
