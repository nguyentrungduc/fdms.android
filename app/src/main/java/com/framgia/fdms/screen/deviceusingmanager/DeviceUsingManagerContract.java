package com.framgia.fdms.screen.deviceusingmanager;

import android.content.Intent;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.AssignmentResponse;
import com.framgia.fdms.data.model.Device;
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

        void onChooseBranchClick();

        void onItemDeviceClick(AssignmentResponse device);

        void showProgress();

        void hideProgress();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void setAllowLoadMore(boolean allowLoadMore);

        void onGetDeviceSuccess(Device device);

        void onGetDeviceFailure(String message);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {

        void getDeviceUsingHistory(DeviceUsingHistoryFilter filter);

        void loadMoreData();

        void getDeviceByDeviceId(int deviceId);
    }
}
