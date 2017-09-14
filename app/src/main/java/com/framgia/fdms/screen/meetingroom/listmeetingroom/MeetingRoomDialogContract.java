package com.framgia.fdms.screen.meetingroom.listmeetingroom;

import android.os.Parcelable;
import com.framgia.fdms.data.model.MeetingRoom;

/**
 * Created by Vinh on 14/09/2017.
 */

public interface MeetingRoomDialogContract {
    void onCancelClick();

    void onSubmitClick();

    /**
     * this interface implement all methods to solve data
     * after pressing submitting button of dialog
     */
    interface ActionCallback extends Parcelable {
        void onAddCallback(MeetingRoom meetingRoom);

        void onEditCallback(MeetingRoom oldMeetingRoom, MeetingRoom newMeetingRoom);
    }
}
