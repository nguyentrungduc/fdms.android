package com.framgia.fdms.screen.deviceselection;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Device;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface DeviceSelectionContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onSearchData(String newText);

        void showProgressbar();

        void onGetDeviceSucces(List<Device> devices);

        void hideProgressbar();

        void onError(String message);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getListDevice(String deviceName, int categoryId, int statusId, int page, int perPage);

        void getData(String keyWord);

        void loadMoreData();
    }
}
