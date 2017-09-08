package com.framgia.fdms.screen.meetingroom.listmeetingroom;

import android.content.Context;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.Toast;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.data.model.MeetingRoom;
import java.util.List;

import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Exposes the data to be used in the ListMeetingRoom screen.
 */

public class ListMeetingRoomViewModel implements ListMeetingRoomContract.ViewModel,
        BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<MeetingRoom> {

    private Context mContext;
    private ListMeetingRoomContract.Presenter mPresenter;
    private ListMeetingRoomAdapter mListMeetingRoomAdapter;
    private ObservableField<Integer> mProgressBarVisibility;
    private int mPage;

    ListMeetingRoomViewModel(Context context) {
        mContext = context;
        mListMeetingRoomAdapter = new ListMeetingRoomAdapter(mContext);
        mListMeetingRoomAdapter.setItemClickListener(this);
        mProgressBarVisibility = new ObservableField<>();
        mPage = FIRST_PAGE;
        mPresenter.getListMeetingRoom("", mPage, PER_PAGE);
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

    @Override
    public void onItemRecyclerViewClick(MeetingRoom item) {
        //TODO: Go to FragmentDetailMeetingRoom
    }

    public ListMeetingRoomAdapter getListMeetingRoomAdapter() {
        return mListMeetingRoomAdapter;
    }

    @Override
    public void onGetListMeetingRoomSuccess(List<MeetingRoom> meetingRooms) {
        mListMeetingRoomAdapter.onUpdatePage(meetingRooms);
    }

    @Override
    public void onGetListMeetingRoomError(String error) {
        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressbar() {
        mProgressBarVisibility.set(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        mProgressBarVisibility.set(View.GONE);
    }

    public ObservableField<Integer> getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public void onAddMeetingRoomClick() {
        // TODO: Open meeting room form
    }
}
