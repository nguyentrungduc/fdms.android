package com.framgia.fdms.screen.device.listdevice;

import android.content.Intent;
import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ListDeviceContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onDeviceLoaded(List<Device> devices);

        void showProgressbar();

        void onError(String msg);

        void hideProgressbar();

        void onSearch(String keyWord);

        void onChooseCategoryClick();

        void onChooseStatusClick();

        void onChooseMakerClick();

        void onChooseVendorClick();

        void onChooseMeetingRoomClick();

        void onClearFilterClick();

        void onReset();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void setupFloatingActionsMenu(User user);

        void loadData();

        void onStartReturnDevice(FloatingActionsMenu floatingActionsMenu);

        void onRegisterDeviceClick(FloatingActionsMenu floatingActionsMenu);

        void getDataWithDevice(Device device);

        void onStartGetData();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void loadMoreData();

        void getData(DeviceFilterModel filterModel, int page);

        void getCurrentUser();

        void getDevicesBorrow();
    }
}
