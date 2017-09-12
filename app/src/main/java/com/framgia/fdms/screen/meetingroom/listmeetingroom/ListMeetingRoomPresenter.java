package com.framgia.fdms.screen.meetingroom.listmeetingroom;

import com.framgia.fdms.data.model.MeetingRoom;
import com.framgia.fdms.data.source.MeetingRoomRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link ListMeetingRoomFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
final class ListMeetingRoomPresenter implements ListMeetingRoomContract.Presenter {
    private static final String TAG = ListMeetingRoomPresenter.class.getName();

    private final ListMeetingRoomContract.ViewModel mViewModel;
    private MeetingRoomRepository mMeetingRoomRepository;
    private CompositeDisposable mCompositeSubscriptions;

    ListMeetingRoomPresenter(ListMeetingRoomContract.ViewModel viewModel,
        MeetingRoomRepository meetingRoomRepository) {
        mViewModel = viewModel;
        mMeetingRoomRepository = meetingRoomRepository;
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
    public void getListMeetingRoom(String roomName, int page, int perPage) {
        Disposable subscription = mMeetingRoomRepository.getListMeetingRoom(roomName, page, perPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgressbar();
                }
            })
            .subscribe(new Consumer<List<MeetingRoom>>() {
                @Override
                public void accept(List<MeetingRoom> meetingRooms) throws Exception {
                    mViewModel.onGetListMeetingRoomSuccess(meetingRooms);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetListMeetingRoomError(error.getMessage());
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
