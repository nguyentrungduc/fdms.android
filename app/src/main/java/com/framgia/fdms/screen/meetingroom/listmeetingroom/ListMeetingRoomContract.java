package com.framgia.fdms.screen.meetingroom.listmeetingroom;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Producer;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ListMeetingRoomContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetListMeetingRoomSuccess(List<Producer> meetingRooms);

        void onGetListMeetingRoomError(String error);

        void onAddMeetingRoomSuccess(Producer meetingRoom);

        void onAddMeetingRoomFailed(String message);

        void onDeleteMeetingRoomSuccess(Producer meetingRoom);

        void onDeleteMeetingRoomFailed(String message);

        void onUpdateMeetingRoomSuccess(Producer meetingRoom, String message);

        void onUpdateMeetingRoomFailed(String message);

        void showProgressbar();

        void hideProgressbar();

        void onAddMeetingRoomClick();

        void onEditMeetingRoomClick(Producer meetingRoom);

        void onDeleteMeetingRoomClick(Producer meetingRoom);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getListMeetingRoom(String roomName, int page, int perPage);

        void addMeetingRoom(Producer meetingRoom);

        void editMeetingRoom(Producer meetingRoom);

        void deleteMeetingRoom(Producer meetingRoom);
    }
}
