package com.framgia.fdms.screen.selection;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.framgia.fdms.BR;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.utils.navigator.Navigator;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.framgia.fdms.screen.selection.SelectionType.ASSIGNEE;
import static com.framgia.fdms.screen.selection.SelectionType.BRANCH;
import static com.framgia.fdms.screen.selection.SelectionType.CATEGORY;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_GROUP;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_GROUP_DIALOG;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_USING_HISTORY;
import static com.framgia.fdms.screen.selection.SelectionType.MARKER;
import static com.framgia.fdms.screen.selection.SelectionType.MEETING_ROOM;
import static com.framgia.fdms.screen.selection.SelectionType.RELATIVE_STAFF;
import static com.framgia.fdms.screen.selection.SelectionType.STATUS;
import static com.framgia.fdms.screen.selection.SelectionType.STATUS_REQUEST;
import static com.framgia.fdms.screen.selection.SelectionType.USER_BORROW;
import static com.framgia.fdms.screen.selection.SelectionType.VENDOR;
import static com.framgia.fdms.utils.Constant.NONE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.TITLE_ALL;
import static com.framgia.fdms.utils.Constant.TITLE_NA;

/**
 * Exposes the data to be used in the StatusSelection screen.
 */

public class SelectionViewModel extends BaseObservable implements SelectionContract.ViewModel {
    public static final String BUNDLE_DATA = "BUNDLE_DATA";
    private SelectionContract.Presenter mPresenter;
    private SelectionAdapter mAdapter;
    private AppCompatActivity mActivity;
    private Navigator mNavigator;
    private boolean mIsLoadMore;
    private int mLoadingMoreVisibility;
    private int mSelectedType;

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

    public SelectionViewModel(AppCompatActivity activity) {
        mAdapter = new SelectionAdapter(new ArrayList<Status>());
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
    public void setPresenter(SelectionContract.Presenter presenter) {
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
    public SelectionAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(SelectionAdapter adapter) {
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
        if (mAdapter.getItemCount() == 0) {
            switch (getSelectedType()) {
                case STATUS:
                case VENDOR:
                case MARKER:
                case MEETING_ROOM:
                case STATUS_REQUEST:
                case RELATIVE_STAFF:
                    data.add(0, new Producer(OUT_OF_INDEX, TITLE_NA));
                    break;
                case DEVICE_GROUP:
                    data.add(0, new Producer(OUT_OF_INDEX, TITLE_ALL));
                    break;
                case ASSIGNEE:
                case USER_BORROW:
                    data.add(0, new Producer(OUT_OF_INDEX, NONE));
                    break;
                case BRANCH:
                case DEVICE_GROUP_DIALOG:
                case DEVICE_USING_HISTORY:
                case CATEGORY:
                    break;
                default:
                    break;
            }
        }
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

    @Bindable
    public int getSelectedType() {
        return mSelectedType;
    }

    public void setSelectedType(int selectedType) {
        mSelectedType = selectedType;
        notifyPropertyChanged(BR.selectedType);
    }
}
