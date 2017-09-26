package com.framgia.fdms.screen.meetingroom.detailmeetingroom;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.framgia.fdms.BR;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.screen.devicedetail.DeviceDetailActivity;
import com.framgia.fdms.utils.Constant;
import com.framgia.fdms.utils.navigator.Navigator;
import java.util.ArrayList;
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
    private Producer mMeetingRoom;
    private Navigator mNavigator;
    private ListDeviceAdapter mListDeviceAdapter;
    private int mPage;
    private boolean mIsRefresh;
    private boolean mIsLoadingMore;
    private boolean mIsAllowLoadMore;
    private boolean mIsVisibleLayoutDataNotFound;
    private String mNotificationLoadData;
    private List<Device> mDevices;

    DetailMeetingRoomViewModel(Context context, Producer meetingRoom, Navigator navigator) {
        mContext = context;
        mDevices = new ArrayList<>();
        if (meetingRoom == null) {
            return;
        }
        mMeetingRoom = meetingRoom;
        mNavigator = navigator;
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
        mPresenter.getListDevice(mMeetingRoom.getId(), mPage, Constant.PER_PAGE);
    }

    public Producer getMeetingRoom() {
        return mMeetingRoom;
    }

    @Override
    public void onItemRecyclerViewClick(Device device) {
        mContext.startActivity(DeviceDetailActivity.getInstance(mContext, device));
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

    private void setLoadingMore(boolean loadingMore) {
        mIsLoadingMore = loadingMore;
        notifyPropertyChanged(BR.loadingMore);
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener =
        new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = FIRST_PAGE;
                mListDeviceAdapter.clear();
                mPresenter.getListDevice(mMeetingRoom.getId(), mPage, Constant.PER_PAGE);
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
            if (mIsAllowLoadMore
                && !isLoadingMore()
                && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
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
        mPresenter.getListDevice(mMeetingRoom.getId(), mPage, Constant.PER_PAGE);
    }

    @Override
    public void onGetListDeviceSuccess(List<Device> devices) {
        if (devices == null) {
            return;
        }
        setLoadingMore(false);
        setRefresh(false);
        mDevices.addAll(devices);
        mListDeviceAdapter.onUpdatePage(devices);
    }

    @Override
    public void onGetListDeviceError(String error) {
        if (mDevices.size() == 0) {
            setVisibleLayoutDataNotFound(true);
            setNotificationLoadData(mContext.getString(R.string.msg_nothing_here));
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
        }
        hideProgressbar();
        setRefresh(false);
        setLoadingMore(false);
    }

    @Override
    public void showProgressbar() {
        setLoadingMore(true);
    }

    @Override
    public void hideProgressbar() {
        setLoadingMore(false);
    }

    @Override
    public void setAllowLoadMore(boolean allowLoadMore) {
        mIsAllowLoadMore = allowLoadMore;
    }

    @Bindable
    public boolean isVisibleLayoutDataNotFound() {
        return mIsVisibleLayoutDataNotFound;
    }

    private void setVisibleLayoutDataNotFound(boolean visibleLayoutDataNotFound) {
        mIsVisibleLayoutDataNotFound = visibleLayoutDataNotFound;
        notifyPropertyChanged(BR.visibleLayoutDataNotFound);
    }

    @Bindable
    public String getNotificationLoadData() {
        return mNotificationLoadData;
    }

    public void setNotificationLoadData(String notificationLoadData) {
        mNotificationLoadData = notificationLoadData;
        notifyPropertyChanged(BR.notificationLoadData);
    }

    public String getTitleToolbar() {
        return mContext.getString(R.string.title_meeting_room_info);
    }

    public void onClickArrowBack(View view) {
        mNavigator.finishActivity();
    }
}
