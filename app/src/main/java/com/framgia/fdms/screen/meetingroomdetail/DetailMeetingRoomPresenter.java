package com.framgia.fdms.screen.meetingroomdetail;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

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
    public void getListDevice(int meetingRoomId, int page, final int perPage) {
        Disposable subscription =
            mDeviceRepository.getListDeviceByMeetingRoomId(meetingRoomId, page, perPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mViewModel.showProgressbar();
                    }
                })
                .subscribe(new Consumer<List<Device>>() {
                    @Override
                    public void accept(@NonNull List<Device> devices) throws Exception {
                        mViewModel.onGetListDeviceSuccess(devices);
                        mViewModel.setAllowLoadMore(devices != null && devices.size() == perPage);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onGetListDeviceError(error.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mViewModel.hideProgressbar();
                    }
                });
        mCompositeSubscriptions.add(subscription);
    }
}
