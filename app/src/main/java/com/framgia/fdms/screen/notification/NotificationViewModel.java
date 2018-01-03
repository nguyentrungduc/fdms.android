package com.framgia.fdms.screen.notification;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Notification;
import com.framgia.fdms.screen.requestdetail.RequestDetailActivity;
import com.framgia.fdms.utils.navigator.Navigator;

import java.util.ArrayList;
import java.util.List;

/**
 * Exposes the data to be used in the Notification screen.
 */

public class NotificationViewModel extends BaseObservable
        implements NotificationContract.ViewModel {

    private final AppCompatActivity mActivity;
    private NotificationContract.Presenter mPresenter;
    private NotificationAdapter mAdapter;
    private boolean mIsRefresh;
    private boolean mIsAllowLoadMore = true;
    private boolean mIsLoadingMore;
    private Navigator mNavigator;

    private SwipeRefreshLayout.OnRefreshListener mRefreshLayout =
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mAdapter.clearData();
                    setRefresh(true);
                    mPresenter.refreshData();
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
                    && !mIsLoadingMore
                    && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                mPresenter.loadMoreNotification();
                mIsLoadingMore = true;
            }
        }
    };

    public NotificationViewModel(AppCompatActivity activity) {
        mActivity = activity;
        mAdapter = new NotificationAdapter(this);
        mNavigator = new Navigator(activity);
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
    public void setPresenter(NotificationContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClickNotification(Notification notification, int position) {
        if (!notification.isChecked()){
            notification.setChecked(true);
            mPresenter.markNotificaionAsRead(notification);
        }
        Intent intent = RequestDetailActivity.
                getInstance(mNavigator.getContext(), notification.getId());
        mNavigator.startActivity(intent);
    }

    @Override
    public void onLoadNotificationSuccess(List<Notification> notifications) {
        setRefresh(false);
        mIsLoadingMore = false;
        mAdapter.addData(notifications);
    }

    @Override
    public void onLoadNotificationFails(String msg) {
        setRefresh(false);
        mIsLoadingMore = false;
        Toast.makeText(mActivity.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Bindable
    public NotificationAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(NotificationAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    public AppCompatActivity getActivity() {
        return mActivity;
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
    public SwipeRefreshLayout.OnRefreshListener getRefreshLayout() {
        return mRefreshLayout;
    }

    public void setRefreshLayout(SwipeRefreshLayout.OnRefreshListener refreshLayout) {
        mRefreshLayout = refreshLayout;
        notifyPropertyChanged(BR.refreshLayout);
    }

    @Bindable
    public RecyclerView.OnScrollListener getScrollListener() {
        return mScrollListener;
    }

    @Override
    public void setAllowLoadMore(boolean allowLoadMore) {
        mIsAllowLoadMore = allowLoadMore;
    }

    @Override
    public void setAllNotificationAsRead() {
        mPresenter.markAllNotificaionAsRead();
    }

    @Override
    public void onMarkAllNotificationAsReadFailed(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void onMarkAllNotificationAsReadSuccessfull(String message) {
        mNavigator.showToast(message);
        mAdapter.clearData();
    }
}
