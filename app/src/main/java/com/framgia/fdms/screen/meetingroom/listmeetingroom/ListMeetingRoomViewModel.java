package com.framgia.fdms.screen.meetingroom.listmeetingroom;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.framgia.fdms.BR;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.screen.meetingroom.detailmeetingroom.DetailMeetingRoomActivity;
import com.framgia.fdms.screen.producer.ProducerDialog;
import com.framgia.fdms.utils.Constant;
import com.framgia.fdms.utils.navigator.Navigator;
import com.framgia.fdms.widget.OnSearchMenuItemClickListener;
import java.util.List;

import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.PER_PAGE;
import static com.framgia.fdms.utils.Constant.TAG_MEETING_ROOM_DIALOG;

/**
 * Exposes the data to be used in the ListMeetingRoom screen.
 */

public class ListMeetingRoomViewModel extends BaseObservable
    implements ListMeetingRoomContract.ViewModel, ProducerDialog.ActionCallback,
    BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Producer>,
    OnSearchMenuItemClickListener, FloatingSearchView.OnSearchListener,
    FloatingSearchView.OnClearSearchActionListener {
    public static final Parcelable.Creator<ListMeetingRoomViewModel> CREATOR =
        new Parcelable.Creator<ListMeetingRoomViewModel>() {
            @Override
            public ListMeetingRoomViewModel createFromParcel(Parcel in) {
                return new ListMeetingRoomViewModel(in);
            }

            @Override
            public ListMeetingRoomViewModel[] newArray(int size) {
                return new ListMeetingRoomViewModel[size];
            }
        };
    private AppCompatActivity mActivity;
    private ListMeetingRoomContract.Presenter mPresenter;
    private ListMeetingRoomAdapter mListMeetingRoomAdapter;
    private ObservableField<Integer> mProgressBarVisibility;
    private int mPage;
    private boolean mIsRefresh;
    private boolean mIsLoadingMore;
    private Navigator mNavigator;
    private String mRoomName;
    private ProducerDialog mMeetingRoomDialog;
    private Producer mMeetingRoom;
    private int mPositionScroll = OUT_OF_INDEX;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener =
        new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = FIRST_PAGE;
                mListMeetingRoomAdapter.clear();
                mPresenter.getListMeetingRoom(getRoomName(), mPage, PER_PAGE);
            }
        };

    ListMeetingRoomViewModel(Activity activity, Navigator navigator) {
        mActivity = (AppCompatActivity) activity;
        mNavigator = navigator;
        mListMeetingRoomAdapter = new ListMeetingRoomAdapter(mActivity, this);
        mProgressBarVisibility = new ObservableField<>();
        mPage = FIRST_PAGE;
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    private ListMeetingRoomViewModel(Parcel in) {
        mPresenter = (ListMeetingRoomContract.Presenter) in.readValue(
            ListMeetingRoomContract.Presenter.class.getClassLoader());
        mActivity = (AppCompatActivity) in.readValue(AppCompatActivity.class.getClassLoader());
        mListMeetingRoomAdapter =
            (ListMeetingRoomAdapter) in.readValue(ListMeetingRoomAdapter.class.getClassLoader());
        mMeetingRoomDialog = (ProducerDialog) in.readValue(ProducerDialog.class.getClassLoader());
    }

    @Override
    public void onItemRecyclerViewClick(Producer item) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.BundleConstant.BUNDLE_MEETING_ROOM, item);
        mNavigator.startActivity(DetailMeetingRoomActivity.class, bundle);
    }

    public ListMeetingRoomAdapter getListMeetingRoomAdapter() {
        return mListMeetingRoomAdapter;
    }

    @Override
    public void onGetListMeetingRoomSuccess(List<Producer> meetingRooms) {
        setLoadingMore(false);
        mListMeetingRoomAdapter.onUpdatePage(meetingRooms);
        setRefresh(false);
    }

    @Override
    public void setPresenter(ListMeetingRoomContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getListMeetingRoom(Constant.BLANK, mPage, PER_PAGE);
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

    @Bindable
    public boolean isLoadingMore() {
        return mIsLoadingMore;
    }

    public void setLoadingMore(boolean loadingMore) {
        mIsLoadingMore = loadingMore;
        notifyPropertyChanged(BR.loadingMore);
    }

    @Override
    public void onGetListMeetingRoomError(String error) {
        Toast.makeText(mActivity, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddMeetingRoomSuccess(Producer meetingRoom) {
        mListMeetingRoomAdapter.addData(0, meetingRoom);
        setPositionScroll(0);
    }

    @Override
    public void onAddMeetingRoomFailed(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void onDeleteMeetingRoomSuccess(Producer meetingRoom) {
        mListMeetingRoomAdapter.removeData(meetingRoom);
    }

    @Override
    public void onDeleteMeetingRoomFailed(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void onUpdateMeetingRoomSuccess(Producer meetingRoom, String message) {
        if (meetingRoom == null || mMeetingRoom == null) {
            return;
        }
        mListMeetingRoomAdapter.updateData(mMeetingRoom, meetingRoom);
        mNavigator.showToast(message);
    }

    @Override
    public void onUpdateMeetingRoomFailed(String message) {
        mNavigator.showToast(message);
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy <= 0) {
                return;
            }
            LinearLayoutManager layoutManager =
                (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
            if (!isLoadingMore() && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                setLoadingMore(true);
                loadMore();
            }
        }
    };

    @Bindable
    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return mOnRefreshListener;
    }

    @Bindable
    public boolean isRefresh() {
        return mIsRefresh;
    }

    public void setRefresh(boolean refresh) {
        mIsRefresh = refresh;
        notifyPropertyChanged(BR.refresh);
    }

    public RecyclerView.OnScrollListener getScrollListener() {
        return mScrollListener;
    }

    @Bindable
    public String getRoomName() {
        return mRoomName;
    }

    public void setRoomName(String roomName) {
        mRoomName = roomName;
        notifyPropertyChanged(BR.roomName);
    }

    private void loadMore() {
        mPage++;
        ((ListMeetingRoomPresenter) mPresenter).getListMeetingRoom(getRoomName(), mPage, PER_PAGE);
    }

    public void onAddMeetingRoomClick() {
        mMeetingRoomDialog = ProducerDialog.newInstant(new Producer(OUT_OF_INDEX, ""),
            mActivity.getResources().getString(R.string.title_add_producer), this);
        mMeetingRoomDialog.show(mActivity.getSupportFragmentManager(), TAG_MEETING_ROOM_DIALOG);
    }

    @Override
    public void onEditMeetingRoomClick(Producer meetingRoom) {
        mMeetingRoomDialog = ProducerDialog.newInstant(meetingRoom,
            mActivity.getResources().getString(R.string.action_edit), this);
        mMeetingRoomDialog.show(mActivity.getSupportFragmentManager(), TAG_MEETING_ROOM_DIALOG);
    }

    @Override
    public void onDeleteMeetingRoomClick(final Producer meetingRoom) {
        new AlertDialog.Builder(mActivity).setTitle(mActivity.getString(R.string.title_delete))
            .setMessage(
                mActivity.getString(R.string.msg_delete_producer) + " " + meetingRoom.getName())
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mPresenter.deleteMeetingRoom(meetingRoom);
                }
            })
            .setNegativeButton(android.R.string.no, null)
            .create()
            .show();
    }

    @Override
    public void onClearSearchClicked() {
        mPage = FIRST_PAGE;
        mListMeetingRoomAdapter.clear();
        setRoomName(Constant.BLANK);
        mPresenter.getListMeetingRoom(getRoomName(), mPage, PER_PAGE);
    }

    @Override
    public void onActionMenuItemSelected(FloatingSearchView searchView, MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            onSearchAction(searchView.getQuery());
        }
    }

    @Override
    public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
        //No ops
    }

    @Override
    public void onSearchAction(String currentQuery) {
        mPage = FIRST_PAGE;
        mListMeetingRoomAdapter.clear();
        setRoomName(currentQuery);
        mPresenter.getListMeetingRoom(getRoomName(), mPage, PER_PAGE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mPresenter);
        dest.writeValue(mActivity);
        dest.writeValue(mListMeetingRoomAdapter);
        dest.writeValue(mMeetingRoomDialog);
    }

    @Override
    public void onAddCallback(Producer meetingRoom) {
        if (meetingRoom == null) {
            return;
        }
        mMeetingRoom = meetingRoom;
        mPresenter.addMeetingRoom(mMeetingRoom);
    }

    @Override
    public void onEditCallback(Producer oldMeetingRoom, Producer newMeetingRoom) {
        if (oldMeetingRoom == null || newMeetingRoom == null) {
            return;
        }
        mMeetingRoom = oldMeetingRoom;
        mPresenter.editMeetingRoom(newMeetingRoom);
    }

    @Bindable
    public int getPositionScroll() {
        return mPositionScroll;
    }

    public void setPositionScroll(int positionScroll) {
        mPositionScroll = positionScroll;
        notifyPropertyChanged(BR.positionScroll);
    }
}
