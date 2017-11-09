package com.framgia.fdms.screen.devicecreation.selectdevicestatus;

import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.screen.baseselection.BaseSelectionActivity;
import com.framgia.fdms.screen.baseselection.BaseSelectionContract;
import com.framgia.fdms.screen.baseselection.BaseSelectionPresenter;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Listens to user actions from the UI ({@link BaseSelectionActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
public class SelectDeviceStatusPresenter extends BaseSelectionPresenter {

    private DeviceRepository mDeviceRepository;
    private int mCurrentDeviceStatus;


    public SelectDeviceStatusPresenter(BaseSelectionContract.ViewModel viewModel,
                                       int curentStatus,
                                       DeviceRepository deviceRepository) {
        super(viewModel);
        mCurrentDeviceStatus = curentStatus;
        mDeviceRepository = deviceRepository;
    }

    @Override
    public void getData(String query) {
        mKeySearch = query;
        Disposable disposable = mDeviceRepository
                .getChangeDeviceStatus(mCurrentDeviceStatus, query)
                .subscribe(new Consumer<List<Status>>() {
                    @Override
                    public void accept(List<Status> statuses) throws Exception {
                        mViewModel.onGetDataSuccess(statuses);
                        mViewModel.setAllowLoadMore(false);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
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
        // no ops
        mViewModel.hideProgress();
    }

}
