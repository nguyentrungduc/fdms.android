package com.framgia.fdms.screen.meetingroom.listmeetingroom;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.framgia.fdms.BR;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.data.model.MeetingRoom;
import com.framgia.fdms.screen.meetingroom.detailmeetingroom.DetailMeetingRoomActivity;
import com.framgia.fdms.utils.Constant;
import com.framgia.fdms.utils.navigator.Navigator;
import java.util.List;

import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.NOT_SEARCH;
import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Exposes the data to be used in the ListMeetingRoom screen.
 */

public class ListMeetingRoomViewModel extends BaseObservable
    implements ListMeetingRoomContract.ViewModel,
    BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<MeetingRoom> {

    private Context mContext;
    private ListMeetingRoomContract.Presenter mPresenter;
    private ListMeetingRoomAdapter mListMeetingRoomAdapter;
    private ObservableField<Integer> mProgressBarVisibility;
    private int mPage;
    private boolean mIsRefresh;
    private boolean mIsLoadingMore;
    private Navigator mNavigator;

    ListMeetingRoomViewModel(Context context, Navigator navigator) {
        mContext = context;
        mNavigator = navigator;
        mListMeetingRoomAdapter = new ListMeetingRoomAdapter(mContext);
        mListMeetingRoomAdapter.setItemClickListener(this);
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

    @Override
    public void setPresenter(ListMeetingRoomContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getListMeetingRoom(NOT_SEARCH, mPage, PER_PAGE);
    }

    @Override
    public void onItemRecyclerViewClick(MeetingRoom item) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.BundleConstant.BUNDLE_MEETING_ROOM, item);
        mNavigator.startActivity(DetailMeetingRoomActivity.class, bundle);
    }

    public ListMeetingRoomAdapter getListMeetingRoomAdapter() {
        return mListMeetingRoomAdapter;
    }

    @Override
    public void onGetListMeetingRoomSuccess(List<MeetingRoom> meetingRooms) {
        setLoadingMore(false);
        mListMeetingRoomAdapter.onUpdatePage(meetingRooms);
        setRefresh(false);
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

    @Bindable
    public boolean isLoadingMore() {
        return mIsLoadingMore;
    }

    public void setLoadingMore(boolean loadingMore) {
        mIsLoadingMore = loadingMore;
        notifyPropertyChanged(BR.loadingMore);
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener =
        new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = FIRST_PAGE;
                mListMeetingRoomAdapter.clear();
                mPresenter.getListMeetingRoom(NOT_SEARCH, mPage, PER_PAGE);
            }
        };

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

    private void loadMore() {
        mPage++;
        mPresenter.getListMeetingRoom(NOT_SEARCH, mPage, PER_PAGE);
    }

    public void onAddMeetingRoomClick() {
        // TODO: Open meeting room form
    }
}
