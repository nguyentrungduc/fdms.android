package com.framgia.fdms.screen.meetingroom.detailmeetingroom;

import com.framgia.fdms.data.source.DeviceRepository;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Listens to user actions from the UI ({@link DetailMeetingRoomActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
final class DetailMeetingRoomPresenter implements DetailMeetingRoomContract.Presenter {
    private static final String TAG = DetailMeetingRoomPresenter.class.getName();

    private final DetailMeetingRoomContract.ViewModel mViewModel;
    private DeviceRepository mDeviceRepository;
    private CompositeDisposable mCompositeSubscriptions;

    DetailMeetingRoomPresenter(DetailMeetingRoomContract.ViewModel viewModel,
            DeviceRepository deviceRepository) {
        mViewModel = viewModel;
        mDeviceRepository = deviceRepository;
        mCompositeSubscriptions = new CompositeDisposable();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mCompositeSubscriptions.clear();
    }

    @Override
    public void getListDevice(String deviceName, int categoryId, int statusId, int page,
            int perPage) {
        //TODO: Get list device
    }
}
