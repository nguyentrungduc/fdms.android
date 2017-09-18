package com.framgia.fdms.screen.deviceusingmanager;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.screen.devicedetail.DeviceDetailActivity;
import com.framgia.fdms.screen.new_selection.StatusSelectionActivity;
import com.framgia.fdms.utils.navigator.Navigator;
import com.framgia.fdms.widget.OnSearchMenuItemClickListener;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static com.framgia.fdms.screen.new_selection.SelectionType.DEVICE_USING_HISTORY;
import static com.framgia.fdms.screen.new_selection.StatusSelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.DRAWER_IS_CLOSE;
import static com.framgia.fdms.utils.Constant.DRAWER_IS_OPEN;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_DEVICE_USING_STATUS;

/**
 * Exposes the data to be used in the DeviceUsing screen.
 */

public class DeviceUsingManagerViewModel extends BaseObservable
    implements DeviceUsingManagerContract.ViewModel, SearchView.OnQueryTextListener,
    FloatingSearchView.OnSearchListener, FloatingSearchView.OnClearSearchActionListener,
    OnSearchMenuItemClickListener, DrawerLayout.DrawerListener {

    private DeviceUsingManagerContract.Presenter mPresenter;
    private String mDrawerStatus = DRAWER_IS_CLOSE;
    private int mEmptyViewVisible = GONE;
    private boolean mIsLoadingMore;

    private DeviceUsingHistoryAdapter mAdapter;
    private DeviceUsingHistoryFilter mFilterModel;
    private boolean mIsFilterChanged;

    private Navigator mNavigator;

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

    public DeviceUsingManagerViewModel(Fragment fragment) {
        mFilterModel = new DeviceUsingHistoryFilter();
        mNavigator = new Navigator(fragment);
        setAdapter(new DeviceUsingHistoryAdapter(new ArrayList<DeviceUsingHistory>()));
        mAdapter.setViewModel(this);
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
    public void setPresenter(DeviceUsingManagerContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getDeviceUsingHistory(mFilterModel);
    }

    @Override
    public void onGetDeviceUsingHistorySuccess(List<DeviceUsingHistory> deviceUsingHistories) {
        mAdapter.updateData(deviceUsingHistories);
        mIsFilterChanged = false;
    }

    @Override
    public void onGetDeviceUsingHistoryFailed(String msg) {
        mNavigator.showToast(msg);
        mIsFilterChanged = false;
    }

    @Override
    public void onClearFilterClick() {
        mFilterModel.initDefaultData();
        mIsFilterChanged = true;
        setDrawerStatus(DRAWER_IS_CLOSE);
    }

    @Override
    public void onChooseStatusClick() {
        mNavigator.startActivityForResult(
            StatusSelectionActivity.getInstance(mNavigator.getContext(), DEVICE_USING_HISTORY),
            REQUEST_DEVICE_USING_STATUS);
    }

    @Override
    public void onItemDeviceClick(Device device) {
        mNavigator.startActivity(DeviceDetailActivity.getInstance(mNavigator.getContext(), device));
    }

    @Override
    public void showProgress() {
        setLoadingMore(true);
    }

    @Override
    public void hideProgress() {
        setLoadingMore(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_DEVICE_USING_STATUS || resultCode != RESULT_OK || data == null) {
            return;
        }
        mIsFilterChanged = true;
        Bundle bundle = data.getExtras();
        Status status = bundle.getParcelable(BUNDLE_DATA);
        mFilterModel.setStatus(status);
        setDrawerStatus(DRAWER_IS_CLOSE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mFilterModel.setDeviceCode(query);
        setDrawerStatus(DRAWER_IS_CLOSE);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mFilterModel.setDeviceCode(newText);
        mIsFilterChanged = true;
        return false;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        // no ops
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        // no ops
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        setDrawerStatus(DRAWER_IS_CLOSE);
        if (mIsFilterChanged) {
            getData();
        }
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        // no ops
    }

    @Override
    public void onClearSearchClicked() {
        mFilterModel.setStaffName("");
        getData();
    }

    @Override
    public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
        // no ops
    }

    @Override
    public void onSearchAction(String s) {
        mAdapter.clearData();
        mFilterModel.setStaffName(s);
        getData();
    }

    private void getData() {
        mAdapter.clearData();
        mIsFilterChanged = true;
        mPresenter.getDeviceUsingHistory(mFilterModel);
    }

    @Override
    public void onActionMenuItemSelected(FloatingSearchView searchView, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                mFilterModel.setStaffName(searchView.getQuery());
                getData();
                break;
            case R.id.action_filter:
                setDrawerStatus(
                    mDrawerStatus == DRAWER_IS_CLOSE ? DRAWER_IS_OPEN : DRAWER_IS_CLOSE);
                break;
            default:
                break;
        }
    }

    @Bindable
    public String getDrawerStatus() {
        return mDrawerStatus;
    }

    public void setDrawerStatus(String drawerStatus) {
        mDrawerStatus = drawerStatus;
        notifyPropertyChanged(BR.drawerStatus);
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
    public DeviceUsingHistoryAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(DeviceUsingHistoryAdapter adapter) {
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

    @Bindable
    public RecyclerView.OnScrollListener getScrollListenner() {
        return mScrollListenner;
    }

    public void setScrollListenner(RecyclerView.OnScrollListener scrollListenner) {
        mScrollListenner = scrollListenner;
        notifyPropertyChanged(BR.scrollListenner);
    }

    @Bindable
    public DeviceUsingHistoryFilter getFilterModel() {
        return mFilterModel;
    }

    public void setFilterModel(DeviceUsingHistoryFilter filterModel) {
        mFilterModel = filterModel;
        notifyPropertyChanged(BR.filterModel);
    }
}
