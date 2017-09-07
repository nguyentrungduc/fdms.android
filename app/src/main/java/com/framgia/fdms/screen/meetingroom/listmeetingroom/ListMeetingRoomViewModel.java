package com.framgia.fdms.screen.meetingroom.listmeetingroom;

/**
 * Exposes the data to be used in the ListMeetingRoom screen.
 */

public class ListMeetingRoomViewModel implements ListMeetingRoomContract.ViewModel {

    private ListMeetingRoomContract.Presenter mPresenter;

    public ListMeetingRoomViewModel() {
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(ListMeetingRoomContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
