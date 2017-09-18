package com.framgia.fdms.screen.deviceusingmanager;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface DeviceUsingManagerContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {

        void onGetDeviceUsingHistorySuccess(List<DeviceUsingHistory> deviceUsingHistories);

        void onGetDeviceUsingHistoryFailed(String msg);

        void onClearFilterClick();

        void onChooseStatusClick();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {

        void getDeviceUsingHistory();

        void loadMoreData();
    }
}
