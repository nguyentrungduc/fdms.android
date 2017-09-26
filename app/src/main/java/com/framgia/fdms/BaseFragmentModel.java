package com.framgia.fdms;

/**
 * Created by beepi on 09/05/2017.
 */

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseFragmentModel extends BaseObservable
    implements BaseFragmentContract.ViewModel {
    protected boolean mIsLoadMore;
    protected boolean mIsAllowLoadMore;
    protected BaseFragmentContract.Presenter mPresenter;
    private ObservableField<Integer> mProgressBarVisibility = new ObservableField<>();
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
                && !mIsLoadMore
                && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                setLoadMore(true);
                mPresenter.onLoadMore();
            }
        }
    };

    public RecyclerView.OnScrollListener getScrollListenner() {
        return mScrollListenner;
    }

    @Override
    public void showProgressbar() {
        mProgressBarVisibility.set(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        mProgressBarVisibility.set(View.GONE);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {

    }

    @Override
    public void setPresenter(BaseFragmentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public ObservableField<Integer> getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    @Bindable
    public boolean isLoadMore() {
        return mIsLoadMore;
    }

    public void setLoadMore(boolean loadMore) {
        mIsLoadMore = loadMore;
        notifyPropertyChanged(BR.loadMore);
    }
}
