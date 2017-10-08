package com.framgia.fdms.screen.devicedetail;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.User;
import com.github.clans.fab.FloatingActionMenu;

/**
 * This specifies the contract between the view and the presenter.
 */
interface DeviceDetailContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onEditDevice(FloatingActionMenu floatingActionsMenu);

        void onGetDeviceSuccess(Device device);

        void onGetDeviceError(String error);

        void onGetUserSuccess(User user);

        void onGetUserError(String error);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getDevice(Device device);

        void getCurrentUser();
    }
}
