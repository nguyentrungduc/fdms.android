package com.framgia.fdms.screen.meetingroom.detailmeetingroom;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.framgia.fdms.BR;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.MeetingRoom;
import java.util.List;

import static com.framgia.fdms.utils.Constant.FIRST_PAGE;

/**
 * Exposes the data to be used in the DetailMeetingRoom screen.
 */

public class DetailMeetingRoomViewModel extends BaseObservable
    implements DetailMeetingRoomContract.ViewModel,
    BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Device> {

    private Context mContext;
    private DetailMeetingRoomContract.Presenter mPresenter;
    private MeetingRoom mMeetingRoom;
    private ListDeviceAdapter mListDeviceAdapter;
    private int mPage;
    private boolean mIsRefresh;
    private boolean mIsLoadingMore;
    private boolean mProgressBarVisibility;

    public DetailMeetingRoomViewModel(Context context, MeetingRoom meetingRoom) {
        mContext = context;
        if (meetingRoom == null) {
            return;
        }
        mMeetingRoom = meetingRoom;
        mListDeviceAdapter = new ListDeviceAdapter(context);
        mListDeviceAdapter.setItemClickListener(this);
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
    public void setPresenter(DetailMeetingRoomContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public MeetingRoom getMeetingRoom() {
        return mMeetingRoom;
    }

    @Override
    public void onItemRecyclerViewClick(Device item) {
        //TODO: Open Detail Device Page
    }

    public ListDeviceAdapter getListDeviceAdapter() {
        return mListDeviceAdapter;
    }

    @Bindable
    public boolean isRefresh() {
        return mIsRefresh;
    }

    public void setRefresh(boolean refresh) {
        mIsRefresh = refresh;
        notifyPropertyChanged(BR.refresh);
    }

    @Bindable
    public boolean isLoadingMore() {
        return mIsLoadingMore;
    }

    public void setLoadingMore(boolean loadingMore) {
        mIsLoadingMore = loadingMore;
        notifyPropertyChanged(BR.loadingMore);
    }

    @Bindable
    public boolean isProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public void setProgressBarVisibility(boolean progressBarVisibility) {
        mProgressBarVisibility = progressBarVisibility;
        notifyPropertyChanged(BR.progressBarVisibility);
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener =
        new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = FIRST_PAGE;
                mListDeviceAdapter.clear();
                //TODO: get list device
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

    public RecyclerView.OnScrollListener getScrollListener() {
        return mScrollListener;
    }

    private void loadMore() {
        mPage++;
        //TODO: get list device
    }

    @Override
    public void onGetListDeviceSuccess(List<Device> devices) {
        setLoadingMore(false);
        mListDeviceAdapter.onUpdatePage(devices);
        setRefresh(false);
    }

    @Override
    public void onGetListDeviceError(String error) {
        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressbar() {
        setProgressBarVisibility(true);
    }

    @Override
    public void hideProgressbar() {
        setProgressBarVisibility(false);
    }
}
