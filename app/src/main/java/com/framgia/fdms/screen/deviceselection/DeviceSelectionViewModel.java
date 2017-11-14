package com.framgia.fdms.screen.deviceselection;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.framgia.fdms.BR;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.screen.deviceselection.filter.BottomFilterAdapter;
import com.framgia.fdms.screen.deviceselection.filter.BottomFilterDialog;
import com.framgia.fdms.utils.navigator.Navigator;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_DEVICES;
import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * Exposes the data to be used in the Deviceselection screen.
 */
public class DeviceSelectionViewModel extends BaseObservable
        implements DeviceSelectionContract.ViewModel,
        BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Device>, BottomSheetCallback {
    private DeviceSelectionContract.Presenter mPresenter;
    private Navigator mNavigator;
    private AppCompatActivity mActivity;
    private DeviceSelectionAdapter mAdapter;
    private boolean mIsLoadingMore;
    private boolean mIsRefresh;
    private int mEmptyViewVisible = View.GONE;

    private BottomFilterAdapter mCategoryAdapter;
    private BottomFilterAdapter mGroupAdapter;

    private Status mGroup;
    private String mKeySearch;

    private int mPage = FIRST_PAGE;
    private boolean mIsAllowLoadMore = true;

    private int mBottomSheetState = BottomSheetBehavior.STATE_COLLAPSED;

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
            if (mIsAllowLoadMore &&
                    !mIsLoadingMore &&
                    (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
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
        mCategoryAdapter = new BottomFilterAdapter(new ArrayList<Producer>(), mOnItemCategoryClicked);
        mGroupAdapter = new BottomFilterAdapter(new ArrayList<Producer>(), mOnItemGroupClicked);
        updateDefaultAdapter();

    }

    private BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Status> mOnItemCategoryClicked =
            new BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Status>() {

                @Override
                public void onItemRecyclerViewClick(Status item) {
                }
            };

    private BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Status> mOnItemGroupClicked =
            new BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Status>() {

                @Override
                public void onItemRecyclerViewClick(Status item) {
                    if (mGroup.getId() == item.getId()) {
                        return;
                    }
                    mGroup = item;
                    mCategoryAdapter.setSelectedPos(0);

                    if (mGroup.getId() == OUT_OF_INDEX) {
                        mCategoryAdapter.clearData();
                        updateDefaultCategoryAdapter();
                        return;
                    }


                    mCategoryAdapter.clearData();
                    mPage = FIRST_PAGE;
                    mPresenter.getCategory(mGroup.getId(), mPage);
                }
            };

    @Override
    public void updateDefaultAdapter() {
        updateDefaultCategoryAdapter();
        updateDefaultGroupAdapter();
    }

    @Override
    public void onGetCategoryError(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void updateGroupData(List<Producer> producers) {
        mGroupAdapter.updateData(producers);
    }

    @Override
    public void onGetGroupError(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void onActionFilterClick() {
        setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onQuerryChange(String newText) {
        mKeySearch = newText;
    }

    @Override
    public void setAllowLoadMore(boolean isAllow) {
        mIsAllowLoadMore = isAllow;
    }

    public void onApplyFilterClicked() {
        setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void onResetFilterClicked() {
        mCategoryAdapter.clearData();
        mGroupAdapter.clearData();
        updateDefaultAdapter();
        resetFilter();
    }

    private void resetFilter() {
        mPresenter.getGroup();
    }

    private void updateDefaultCategoryAdapter() {
        if (mCategoryAdapter.getItemCount() == 0) {
            mCategoryAdapter.setSelectedPos(0);
        }
    }

    private void updateDefaultGroupAdapter() {
        if (mGroupAdapter.getItemCount() == 0) {
            mGroup = new Producer(OUT_OF_INDEX,
                    mActivity.getString(R.string.action_all));
            mGroupAdapter.updateData((Producer) mGroup);
            mGroupAdapter.setSelectedPos(0);
        }
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
        mKeySearch = newText;
        mAdapter.clear();
        mPresenter.getData(newText, mCategoryAdapter.getSelectedItemId());
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
    public void updateCategoryData(List<Producer> producers) {
        mCategoryAdapter.updateData(producers);
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

    @Bindable
    public BottomFilterAdapter getCategoryAdapter() {
        return mCategoryAdapter;
    }

    public void setCategoryAdapter(BottomFilterAdapter categoryAdapter) {
        mCategoryAdapter = categoryAdapter;
        notifyPropertyChanged(BR.categoryAdapter);
    }

    @Bindable
    public BottomFilterAdapter getGroupAdapter() {
        return mGroupAdapter;
    }

    public void setGroupAdapter(BottomFilterAdapter groupAdapter) {
        mGroupAdapter = groupAdapter;
        notifyPropertyChanged(BR.groupAdapter);
    }

    @Bindable
    public int getBottomSheetState() {
        return mBottomSheetState;
    }

    public void setBottomSheetState(int bottomSheetState) {
        mBottomSheetState = bottomSheetState;
        notifyPropertyChanged(BR.bottomSheetState);
    }

    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {
        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
            mAdapter.clear();
            mPresenter.getData(mKeySearch, mCategoryAdapter.getSelectedItemId());
        }
    }
}
