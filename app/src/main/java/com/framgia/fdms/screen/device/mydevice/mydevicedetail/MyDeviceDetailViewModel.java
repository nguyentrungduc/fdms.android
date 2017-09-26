package com.framgia.fdms.screen.device.mydevice.mydevicedetail;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.framgia.fdms.BR;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.screen.device.listdevice.ItemDeviceClickListenner;
import com.framgia.fdms.screen.devicedetail.DeviceDetailActivity;
import com.framgia.fdms.utils.navigator.Navigator;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Exposes the data to be used in the MyDeviceDetail screen.
 */

public class MyDeviceDetailViewModel extends BaseObservable
    implements MyDeviceDetailContract.ViewModel, ItemDeviceClickListenner {

    private static final String TAG = "MyDeviceDetailViewModel";

    private MyDeviceDetailContract.Presenter mPresenter;
    private int mProgressVisibility = GONE;
    private Navigator mNavigator;
    private MyDeviceDetailAdapter mAdapter;
    private boolean mIsLoadingMore;
    private boolean mIsAllowLoadMore;
    private int mEmptyStateVisibility = GONE;

    private RecyclerView.OnScrollListener mScrollListenner = new RecyclerView.OnScrollListener() {
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
            if (mIsAllowLoadMore
                && !mIsLoadingMore
                && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                mIsLoadingMore = true;
                mPresenter.loadMoreData();
            }
        }
    };

    public MyDeviceDetailViewModel(Fragment fragment) {
        mNavigator = new Navigator(fragment);
        setAdapter(new MyDeviceDetailAdapter(new ArrayList<Device>(), this));
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
    public void setPresenter(MyDeviceDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetDataFailure(String msg) {
        mNavigator.showToast(msg);
        mIsLoadingMore = false;
        setEmptyStateVisibility(mAdapter != null && mAdapter.getItemCount() == 0 ? VISIBLE : GONE);
    }

    @Override
    public void hideProgress() {
        setProgressVisibility(GONE);
    }

    @Override
    public void showProgress() {
        setProgressVisibility(VISIBLE);
    }

    @Override
    public void onGetDeviceSuccess(List<Device> devices) {
        mIsLoadingMore = false;
        mAdapter.updateData(devices);
        setEmptyStateVisibility(mAdapter != null && mAdapter.getItemCount() == 0 ? VISIBLE : GONE);
    }

    @Override
    public void setAllowLoadMore(boolean allowLoadMore) {
        mIsAllowLoadMore = allowLoadMore;
    }

    @Bindable
    public int getProgressVisibility() {
        return mProgressVisibility;
    }

    public void setProgressVisibility(int progressVisibility) {
        mProgressVisibility = progressVisibility;
        notifyPropertyChanged(BR.progressVisibility);
    }

    @Bindable
    public MyDeviceDetailAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(MyDeviceDetailAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Override
    public void onItemDeviceClick(Device device) {
        mNavigator.startActivity(DeviceDetailActivity.getInstance(mNavigator.getContext(), device));
    }

    @Bindable
    public RecyclerView.OnScrollListener getScrollListenner() {
        return mScrollListenner;
    }

    public void setScrollListenner(RecyclerView.OnScrollListener scrollListenner) {
        mScrollListenner = scrollListenner;
        notifyPropertyChanged(BR.scrollListenner);
    }

    @Bindable
    public int getEmptyStateVisibility() {
        return mEmptyStateVisibility;
    }

    public void setEmptyStateVisibility(int emptyStateVisibility) {
        mEmptyStateVisibility = emptyStateVisibility;
        notifyPropertyChanged(BR.emptyStateVisibility);
    }
}
