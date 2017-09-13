package com.framgia.fdms.screen.deviceselection;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.framgia.fdms.BR;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.utils.navigator.Navigator;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_DEVICES;

/**
 * Exposes the data to be used in the Deviceselection screen.
 */
public class DeviceSelectionViewModel extends BaseObservable
    implements DeviceSelectionContract.ViewModel,
    BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Device> {
    private DeviceSelectionContract.Presenter mPresenter;
    private Navigator mNavigator;
    private AppCompatActivity mActivity;
    private DeviceSelectionAdapter mAdapter;
    private boolean mIsLoadingMore;
    private boolean mIsRefresh;
    private int mEmptyViewVisible = View.GONE;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener =
        new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                mPresenter.getData(null);
            }
        };
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
            if (!mIsLoadingMore && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                setLoadingMore(true);
                mPresenter.loadMoreData();
            }
        }
    };

    public DeviceSelectionViewModel(AppCompatActivity activity) {
        mActivity = activity;
        mNavigator = new Navigator(activity);
        mAdapter =
            new DeviceSelectionAdapter(mNavigator.getContext(), this, new ArrayList<Device>());
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
    public void setPresenter(DeviceSelectionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSearchData(String newText) {
        mAdapter.clear();
        mPresenter.getData(newText);
    }

    @Override
    public void showProgressbar() {
        setLoadingMore(true);
    }

    @Override
    public void onGetDeviceSucces(List<Device> devices) {
        setEmptyViewVisible(
            devices.isEmpty() && mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        setLoadingMore(false);
        mAdapter.onUpdatePage(devices);
        setRefresh(false);
    }

    @Override
    public void hideProgressbar() {
        setLoadingMore(false);
    }

    @Override
    public void onError(String message) {
        setLoadingMore(false);
        mNavigator.showToast(message);
        setRefresh(false);
    }

    @Override
    public void onItemRecyclerViewClick(Device item) {
        if (item == null) {
            return;
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_DEVICES, item);
        intent.putExtras(bundle);
        mNavigator.finishActivityWithResult(intent, RESULT_OK);
    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }

    @Bindable
    public DeviceSelectionAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(DeviceSelectionAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Bindable
    public boolean isLoadingMore() {
        return mIsLoadingMore;
    }

    public void setLoadingMore(boolean loadingMore) {
        mIsLoadingMore = loadingMore;
        notifyPropertyChanged(BR.loadingMore);
    }

    public RecyclerView.OnScrollListener getScrollListenner() {
        return mScrollListenner;
    }

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

    @Bindable
    public int getEmptyViewVisible() {
        return mEmptyViewVisible;
    }

    public void setEmptyViewVisible(int emptyViewVisible) {
        mEmptyViewVisible = emptyViewVisible;
        notifyPropertyChanged(BR.emptyViewVisible);
    }
}
