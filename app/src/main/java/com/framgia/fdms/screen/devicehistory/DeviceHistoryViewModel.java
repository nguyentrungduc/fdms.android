package com.framgia.fdms.screen.devicehistory;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.DeviceUsingHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Exposes the data to be used in the Devicehistory screen.
 */
public class DeviceHistoryViewModel extends BaseObservable
    implements DeviceHistoryContract.ViewModel {
    private DeviceHistoryContract.Presenter mPresenter;
    private ListUserAdapter mAdapter;
    private List<DeviceUsingHistory> mDeviceUsingHistories = new ArrayList<>();
    private AppCompatActivity mActivity;
    private boolean mIsLoadMore;
    private boolean mIsExpanded;

    public DeviceHistoryViewModel(Activity activity) {
        mActivity = (AppCompatActivity) activity;
        mAdapter = new ListUserAdapter(this, mDeviceUsingHistories);
        setLoadMore(false);
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
    public void setPresenter(DeviceHistoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Bindable
    public ListUserAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(ListUserAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Override
    public void onLoadDeviceHistorySuccess(List<DeviceUsingHistory> deviceUsingHistories) {
        setLoadMore(false);
        if (deviceUsingHistories != null) {
            mDeviceUsingHistories.addAll(deviceUsingHistories);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadDeviceHistoryFailed() {
        Snackbar.make(mActivity.findViewById(android.R.id.content), R.string.msg_load_data_fails,
            Snackbar.LENGTH_SHORT).show();
        setLoadMore(false);
    }

    @Bindable
    public boolean isLoadMore() {
        return mIsLoadMore;
    }

    public void setLoadMore(boolean loadMore) {
        mIsLoadMore = loadMore;
        notifyPropertyChanged(BR.loadMore);
    }

    @Bindable
    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setExpanded(boolean expanded) {
        mIsExpanded = expanded;
        notifyPropertyChanged(BR.expanded);
    }

    @Bindable
    public ExpandableListView.OnScrollListener getScrollListener() {
        return mScrollListener;
    }

    private ExpandableListView.OnScrollListener mScrollListener =
        new ExpandableListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (!mIsLoadMore && firstVisibleItem + visibleItemCount >= totalItemCount) {
                    setLoadMore(true);
                    mPresenter.getDeviceUsingHistory();
                }
            }
        };
}
