package com.framgia.fdms.screen.device.listdevice;

import android.content.Intent;
import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.User;
import com.github.clans.fab.FloatingActionMenu;
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

        void onChooseCategoryClick();

        void onChooseStatusClick();

        void onChooseMakerClick();

        void onChooseVendorClick();

        void onChooseMeetingRoomClick();

        void onChooseBranchClick();

        void onClearFilterClick();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void setupFloatingActionsMenu(User user);

        void loadData();

        void onStartReturnDevice(FloatingActionMenu floatingActionsMenu);

        void onRegisterDeviceClick(FloatingActionMenu floatingActionsMenu);

        void onAssignDeviceForNewMemberClick(FloatingActionMenu floatingActionsMenu);

        void onAssignDeviceForMeetingRoomClick(FloatingActionMenu floatingActionsMenu);

        void getDataWithDevice(Device device);

        void onStartGetData();

        void setAllowLoadMore(boolean allowLoadMore);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void loadMoreData();

        void getData(DeviceFilterModel filterModel, int page);

        void getCurrentUser();
    }
}
