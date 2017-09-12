package com.framgia.fdms.screen.meetingroom.detailmeetingroom;

/**
 * Exposes the data to be used in the DetailMeetingRoom screen.
 */

public class DetailMeetingRoomViewModel implements DetailMeetingRoomContract.ViewModel {

    private DetailMeetingRoomContract.Presenter mPresenter;

    public DetailMeetingRoomViewModel() {
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
    public void setPresenter(DetailMeetingRoomContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
