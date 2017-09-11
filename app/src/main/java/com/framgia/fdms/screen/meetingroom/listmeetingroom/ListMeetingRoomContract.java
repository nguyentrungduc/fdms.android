package com.framgia.fdms.screen.meetingroom.listmeetingroom;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.MeetingRoom;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ListMeetingRoomContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetListMeetingRoomSuccess(List<MeetingRoom> meetingRooms);

        void onGetListMeetingRoomError(String error);

        void showProgressbar();

        void hideProgressbar();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getListMeetingRoom(String roomName, int page, int perPage);
    }
}
