package com.framgia.fdms.screen.device.mydevice.mydevicedetail;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface MyDeviceDetailContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetDataFailure(String msg);

        void hideProgress();

        void showProgress();

        void onGetDeviceSuccess(List<Device> devices);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getData();

        void getUser();

        void loadMoreData();
    }
}
