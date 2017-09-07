package com.framgia.fdms.screen.meetingroom.listmeetingroom;

/**
 * Listens to user actions from the UI ({@link ListMeetingRoomFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
final class ListMeetingRoomPresenter implements ListMeetingRoomContract.Presenter {
    private static final String TAG = ListMeetingRoomPresenter.class.getName();

    private final ListMeetingRoomContract.ViewModel mViewModel;

    public ListMeetingRoomPresenter(ListMeetingRoomContract.ViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }
}
