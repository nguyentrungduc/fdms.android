package com.framgia.fdms.screen.meetingroom.detailmeetingroom;

/**
 * Listens to user actions from the UI ({@link DetailMeetingRoomActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
final class DetailMeetingRoomPresenter implements DetailMeetingRoomContract.Presenter {
    private static final String TAG = DetailMeetingRoomPresenter.class.getName();

    private final DetailMeetingRoomContract.ViewModel mViewModel;

    DetailMeetingRoomPresenter(DetailMeetingRoomContract.ViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }
}
