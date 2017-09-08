package com.framgia.fdms.screen.meetingroom.listmeetingroom;

import android.content.Context;
import android.widget.Toast;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.data.model.MeetingRoom;
import com.framgia.fdms.data.source.api.error.BaseException;
import java.util.List;

/**
 * Exposes the data to be used in the ListMeetingRoom screen.
 */

public class ListMeetingRoomViewModel implements ListMeetingRoomContract.ViewModel,
        BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<MeetingRoom> {

    private Context mContext;
    private ListMeetingRoomContract.Presenter mPresenter;
    private ListMeetingRoomAdapter mListMeetingRoomAdapter;

    ListMeetingRoomViewModel(Context context) {
        mContext = context;
        mListMeetingRoomAdapter = new ListMeetingRoomAdapter(mContext);
        mListMeetingRoomAdapter.setItemClickListener(this);
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
    public void onGetListMeetingRoomError(BaseException exception) {
        Toast.makeText(mContext, exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void onAddMeetingRoomClick() {
        // TODO: Open meeting room form
    }
}
