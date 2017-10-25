package com.framgia.fdms.screen.requestdetail.listdeviceassignment;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Device;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ListDeviceAssignmentContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetDeviceDetailSuccess(Device device);

        void onGetDeviceDetailFailure(String message);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getDeviceByDeviceId(int deviceId);
    }
}
