package com.framgia.fdms.screen.new_selection;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.framgia.fdms.BR;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.utils.navigator.Navigator;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Exposes the data to be used in the StatusSelection screen.
 */

public class StatusSelectionViewModel extends BaseObservable
    implements StatusSelectionContract.ViewModel {
    public static final String BUNDLE_DATA = "BUNDLE_DATA";
    private StatusSelectionContract.Presenter mPresenter;
    private StatusSelectionAdapter mAdapter;
    private AppCompatActivity mActivity;
    private Navigator mNavigator;
    private boolean mIsLoadMore;
    private int mLoadingMoreVisibility;

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
            if (!mIsLoadMore && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                setLoadingMoreVisibility(VISIBLE);
                mPresenter.loadMoreData();
            }
        }
    };

    public StatusSelectionViewModel(AppCompatActivity activity) {
        mAdapter = new StatusSelectionAdapter(new ArrayList<Status>());
        mAdapter.setViewModel(this);
        mActivity = activity;
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
    public void setPresenter(StatusSelectionContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getData("");
    }

    public void onSelectedItem(Status data) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_DATA, data);
        intent.putExtras(bundle);
        mNavigator.finishActivityWithResult(intent, RESULT_OK);
    }

    @Bindable
    public StatusSelectionAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(StatusSelectionAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Bindable
    public AppCompatActivity getActivity() {
        return mActivity;
    }

    public void setActivity(AppCompatActivity activity) {
        mActivity = activity;
        notifyPropertyChanged(BR.activity);
    }

    @Override
    public void onSearch(String query) {
        mAdapter.clearData();
        mPresenter.getData(query);
    }

    @Override
    public void onGetDataSuccess(List data) {
        mIsLoadMore = false;
        mAdapter.updateData(data);
    }

    @Override
    public void onGetDataFailed(String msg) {
        mIsLoadMore = false;
        mNavigator.showToast(msg);
    }

    @Override
    public void showProgress() {
        setLoadingMoreVisibility(VISIBLE);
    }

    @Override
    public void hideProgress() {
        setLoadingMoreVisibility(GONE);
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
    public int getLoadingMoreVisibility() {
        return mLoadingMoreVisibility;
    }

    public void setLoadingMoreVisibility(int loadingMoreVisibility) {
        mLoadingMoreVisibility = loadingMoreVisibility;
        notifyPropertyChanged(BR.loadingMoreVisibility);
    }

    @Bindable
    public RecyclerView.OnScrollListener getScrollListener() {
        return mScrollListener;
    }
}
