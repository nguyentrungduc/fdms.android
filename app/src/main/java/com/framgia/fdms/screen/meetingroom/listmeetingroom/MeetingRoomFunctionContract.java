package com.framgia.fdms.screen.meetingroom.listmeetingroom;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.MeetingRoom;

/**
 * Created by Vinh on 14/09/2017.
 */

public class MeetingRoomFunctionContract {
    interface ViewModel<T extends MeetingRoomFunctionContract.MeetingRoomPresenter>
        extends BaseViewModel<MeetingRoomFunctionContract.MeetingRoomPresenter> {
        void onAddMeetingRoomClick();
    }

    interface ViewModelItem<T extends MeetingRoomFunctionContract.MeetingRoomPresenter>
        extends BaseViewModel<MeetingRoomFunctionContract.MeetingRoomPresenter> {
        void onEditMeetingRoomClick(MeetingRoom meetingRoom);

        void onDeleteMeetingRoomClick(MeetingRoom meetingRoom);
    }

    public interface MeetingRoomPresenter extends BasePresenter {
    }
}
