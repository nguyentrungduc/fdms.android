package com.framgia.fdms.screen.devicedetail.usinghistory;

import com.framgia.fdms.BaseFragmentContract;
import com.framgia.fdms.data.model.NewDeviceUsingHistory;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface DeviceUsingHistoryContract {
    /**
     * View.
     */
    interface ViewModel extends BaseFragmentContract.ViewModel {
        void onGetUsingHistoryDeviceSuccess(List<NewDeviceUsingHistory> histories);

        void onGetUsingHistoryDeviceFailed(String msg);

        void setDeviceUsingHistoryPresenter(DeviceUsingHistoryContract.Presenter presenter);

        void onLoadData();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BaseFragmentContract.Presenter {
        void getUsingHistoryDevice();
    }
}
