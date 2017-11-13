package com.framgia.fdms.screen.device.mydevice.mydevicedetail;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.AssignmentResponse;
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

        void onGetDeviceSuccess(List<AssignmentResponse> devices);

        void setAllowLoadMore(boolean allowLoadMore);

        void onItemDeviceClick(AssignmentResponse device);

        void onGetDeviceByIdSuccess(Device device);

        void onGetDeviceByIdFailure(String message);

        void showGetDeviceByIdProgress();

        void hideGetDeviceByIdProgress();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getData();

        void loadMoreData();

        void getDeviceByDeviceId(int deviceId);
    }
}
