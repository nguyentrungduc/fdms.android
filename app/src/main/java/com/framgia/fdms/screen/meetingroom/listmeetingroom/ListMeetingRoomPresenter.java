package com.framgia.fdms.screen.meetingroom.listmeetingroom;

import com.framgia.fdms.data.model.MeetingRoom;
import com.framgia.fdms.data.source.MeetingRoomRepository;
import java.util.List;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Listens to user actions from the UI ({@link ListMeetingRoomFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
final class ListMeetingRoomPresenter implements ListMeetingRoomContract.Presenter {
    private static final String TAG = ListMeetingRoomPresenter.class.getName();

    private final ListMeetingRoomContract.ViewModel mViewModel;
    private MeetingRoomRepository mMeetingRoomRepository;
    private CompositeSubscription mCompositeSubscriptions;

    ListMeetingRoomPresenter(ListMeetingRoomContract.ViewModel viewModel,
            MeetingRoomRepository meetingRoomRepository) {
        mViewModel = viewModel;
        mMeetingRoomRepository = meetingRoomRepository;
        mCompositeSubscriptions = new CompositeSubscription();
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
        Subscription subscription =
                mMeetingRoomRepository.getListMeetingRoom(roomName, page, perPage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                mViewModel.showProgressbar();
                            }
                        })
                        .subscribe(new Subscriber<List<MeetingRoom>>() {
                            @Override
                            public void onCompleted() {
                                mViewModel.hideProgressbar();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mViewModel.onGetListMeetingRoomError(e.getCause().getMessage());
                            }

                            @Override
                            public void onNext(List<MeetingRoom> meetingRooms) {
                                mViewModel.onGetListMeetingRoomSuccess(meetingRooms);
                            }
                        });
        mCompositeSubscriptions.add(subscription);
    }
}
