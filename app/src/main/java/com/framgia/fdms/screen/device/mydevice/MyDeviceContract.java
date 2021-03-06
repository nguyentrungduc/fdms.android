package com.framgia.fdms.screen.device.mydevice;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.User;

/**
 * This specifies the contract between the view and the presenter.
 */
interface MyDeviceContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetUserSuccess(User user);

        void onGetUserFailure(String msg);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getCurrentUser();
    }
}
