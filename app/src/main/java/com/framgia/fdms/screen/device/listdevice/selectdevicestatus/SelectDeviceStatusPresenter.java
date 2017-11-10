package com.framgia.fdms.screen.device.listdevice.selectdevicestatus;

import android.text.TextUtils;

import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.screen.baseselection.BaseSelectionActivity;
import com.framgia.fdms.screen.baseselection.BaseSelectionContract;
import com.framgia.fdms.screen.baseselection.BaseSelectionPresenter;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * Listens to user actions from the UI ({@link BaseSelectionActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
public class SelectDeviceStatusPresenter extends BaseSelectionPresenter {

    private DeviceRepository mDeviceRepository;


    public SelectDeviceStatusPresenter(BaseSelectionContract.ViewModel viewModel,
                                       DeviceRepository deviceRepository) {
        super(viewModel);
        mDeviceRepository = deviceRepository;
    }

    @Override
    public void getData(final String query) {
        mKeySearch = query;
        Disposable disposable = mDeviceRepository
                .getDeviceStatus(query)
                .subscribe(new Consumer<List<Status>>() {
                    @Override
                    public void accept(List<Status> statuses) throws Exception {
                        if (mPage == FIRST_PAGE && TextUtils.isEmpty(query)) {
                            statuses.add(0, new Status(OUT_OF_INDEX,
                                    mViewModel.getString(R.string.action_all)));
                            mViewModel.clearData();
                        }
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
