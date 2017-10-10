package com.framgia.fdms.screen.devicedetail.history;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import com.framgia.fdms.BR;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.DeviceHistoryDetail;
import com.framgia.fdms.databinding.DialogDeviceInformationBinding;
import com.framgia.fdms.utils.navigator.Navigator;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Exposes the data to be used in the DeviceHistoryDetail screen.
 */

public class DeviceDetailHistoryViewModel extends BaseObservable
    implements DeviceDetailHistoryContract.ViewModel,
    BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<DeviceHistoryDetail> {

    private DeviceDetailHistoryContract.Presenter mPresenter;
    private DeviceDetailHistoryAdapter mAdapter;
    private int mEmptyViewVisible = GONE;
    private int mProgressStatus = GONE;
    private Navigator mNavigator;
    private boolean mIsLoadingMore;
    private boolean mIsAllowLoadMore = true;
    private AppCompatActivity mActivity;

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
            if (mIsAllowLoadMore
                && !mIsLoadingMore
                && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                setLoadingMore(true);
                mPresenter.loadMoreData();
            }
        }
    };

    public DeviceDetailHistoryViewModel(Fragment fragment) {
        mActivity = (AppCompatActivity) fragment.getActivity();
        mAdapter = new DeviceDetailHistoryAdapter();
        mAdapter.setListener(this);
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
        mAdapter.addData(deviceHistoryDetails);
        setEmptyViewVisible(mAdapter.getItemCount() != 0 ? GONE : VISIBLE);
    }

    @Override
    public void showProgress() {
        setProgressStatus(VISIBLE);
    }

    @Override
    public void hideProgress() {
        setProgressStatus(GONE);
    }

    @Override
    public void setAllowLoadMore(boolean allowLoadMore) {
        mIsAllowLoadMore = allowLoadMore;
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

    @Override
    public void onItemRecyclerViewClick(DeviceHistoryDetail item) {
        if (item == null || item.getDevice() == null) {
            return;
        }
        Context context = mNavigator.getContext();
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        DialogDeviceInformationBinding binding =
            DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_device_information, null, false);
        binding.setDevice(item.getDevice());
        binding.imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(binding.getRoot());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        BottomSheetBehavior bottomSheetBehavior =
            BottomSheetBehavior.from(((View) binding.getRoot().getParent()));
        bottomSheetBehavior.setPeekHeight(height);

        bottomSheetDialog.show();
    }
}
