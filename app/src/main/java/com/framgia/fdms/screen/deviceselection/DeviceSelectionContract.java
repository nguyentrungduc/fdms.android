package com.framgia.fdms.screen.deviceselection;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.screen.device.listdevice.DeviceFilterModel;
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

        void updateCategoryData(List<Producer> producers);

        void updateDefaultAdapter();

        void onGetCategoryError(String message);

        void updateGroupData(List<Producer> producers);

        void onGetGroupError(String message);

        void onActionFilterClick();

        void onQuerryChange(String newText);

        void setAllowLoadMore(boolean isAllow);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getListDevice(DeviceFilterModel filterModel, int page);

        void getData(String keyWord, int categoryId);

        void loadMoreData();

        void getCategory(int id, int page);

        void getGroup();
    }
}
