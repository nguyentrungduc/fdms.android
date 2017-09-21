package com.framgia.fdms.screen.devicedetail.history;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.framgia.fdms.BR;
import com.framgia.fdms.data.model.DeviceHistoryDetail;
import com.framgia.fdms.utils.navigator.Navigator;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Exposes the data to be used in the DeviceHistoryDetail screen.
 */

public class DeviceDetailHistoryViewModel extends BaseObservable
    implements DeviceDetailHistoryContract.ViewModel {

    private DeviceDetailHistoryContract.Presenter mPresenter;
    private DeviceDetailHistoryAdapter mAdapter;
    private int mEmptyViewVisible = GONE;
    private int mProgressStatus = GONE;
    private Navigator mNavigator;
    private boolean mIsLoadingMore;

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
            int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
            if (!mIsLoadingMore && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                setLoadingMore(true);
                mPresenter.loadMoreData();
            }
        }
    };

    public DeviceDetailHistoryViewModel(Fragment fragment) {
        mAdapter = new DeviceDetailHistoryAdapter();
        mNavigator = new Navigator(fragment);
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
    public void setPresenter(DeviceDetailHistoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetDeviceHistoryFailed(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void onGetDeviceHistorySuccess(List<DeviceHistoryDetail> deviceHistoryDetails) {
        if (deviceHistoryDetails != null && deviceHistoryDetails.size() != 0) {
            setEmptyViewVisible(GONE);
            mAdapter.addData(deviceHistoryDetails);
        } else {
            setEmptyViewVisible(View.VISIBLE);
        }
    }

    @Override
    public void showProgress() {
        setProgressStatus(VISIBLE);
    }

    @Override
    public void hideProgress() {
        setProgressStatus(GONE);
    }

    @Bindable
    public DeviceDetailHistoryAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(DeviceDetailHistoryAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Bindable
    public int getEmptyViewVisible() {
        return mEmptyViewVisible;
    }

    public void setEmptyViewVisible(int emptyViewVisible) {
        mEmptyViewVisible = emptyViewVisible;
        notifyPropertyChanged(BR.emptyViewVisible);
    }

    @Bindable
    public int getProgressStatus() {
        return mProgressStatus;
    }

    public void setProgressStatus(int progressStatus) {
        mProgressStatus = progressStatus;
        notifyPropertyChanged(BR.progressStatus);
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
    public RecyclerView.OnScrollListener getScrollListener() {
        return mScrollListener;
    }

    public void setScrollListener(RecyclerView.OnScrollListener scrollListener) {
        mScrollListener = scrollListener;
        notifyPropertyChanged(BR.scrollListener);
    }
}
