package com.framgia.fdms.screen.meetingroom.detailmeetingroom;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Device;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface DetailMeetingRoomContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetListDeviceSuccess(List<Device> devices);

        void onGetListDeviceError(String error);

        void showProgressbar();

        void hideProgressbar();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getListDevice(int meetingRoomId, int page, int perPage);
    }
}
