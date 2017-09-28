package com.framgia.fdms.screen.producer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.framgia.fdms.BR;
import com.framgia.fdms.FDMSApplication;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.screen.selection.SelectionActivity;
import com.framgia.fdms.utils.Constant;
import com.framgia.fdms.utils.navigator.Navigator;
import com.framgia.fdms.widget.OnSearchMenuItemClickListener;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_GROUP;
import static com.framgia.fdms.screen.selection.SelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.DRAWER_IS_CLOSE;
import static com.framgia.fdms.utils.Constant.DRAWER_IS_OPEN;
import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_DEVICE_GROUPS;
import static com.framgia.fdms.utils.Constant.TAG_PRODUCER_DIALOG;

/**
 * Exposes the data to be used in the Producer screen.
 */
public class ProducerViewModel extends BaseObservable
    implements ProducerContract.ViewModel, ProducerDialogContract.ActionCallback,
    FloatingSearchView.OnSearchListener, FloatingSearchView.OnClearSearchActionListener,
    OnSearchMenuItemClickListener, DrawerLayout.DrawerListener {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProducerViewModel> CREATOR =
        new Parcelable.Creator<ProducerViewModel>() {
            @Override
            public ProducerViewModel createFromParcel(Parcel in) {
                return new ProducerViewModel(in);
            }

            @Override
            public ProducerViewModel[] newArray(int size) {
                return new ProducerViewModel[size];
            }
        };
    private ProducerContract.Presenter mPresenter;
    private ProducerAdapter mAdapter;
    private ProducerFragment mFragment;
    private ProducerDialog mProducerDialog;
    private int mPositionScroll = OUT_OF_INDEX;
    private Producer mProducerEdit;
    private boolean mIsLoadMore;
    private boolean mIsAllowLoadMore;
    private int mLoadingMoreVisibility;
    private Navigator mNavigator;
    private boolean mIsShowCategoryFilter;
    private String mDrawerStatus = DRAWER_IS_CLOSE;
    private Producer mGroupType;
    private Context mContext;
    private String mKeySearch;
    private boolean mIsRefresh;

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
                && !mIsLoadMore
                && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                mIsLoadMore = true;
                setLoadingMoreVisibility(View.VISIBLE);
                mPresenter.loadMorePage();
            }
        }
    };

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener =
        new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clearData();
                mPresenter.getProducer(mKeySearch, FIRST_PAGE);
            }
        };

    public ProducerViewModel(ProducerFragment producerFragment) {
        mFragment = producerFragment;
        mContext = producerFragment.getContext();
        mNavigator = new Navigator(mFragment);
        mAdapter =
            new ProducerAdapter(FDMSApplication.getInstant(), this, new ArrayList<Producer>());
        setLoadingMoreVisibility(GONE);
        mGroupType = new Producer(OUT_OF_INDEX, Constant.DeviceUsingStatus.ALL);
    }

    protected ProducerViewModel(Parcel in) {
        mPresenter = (ProducerContract.Presenter) in.readValue(
            ProducerContract.Presenter.class.getClassLoader());
        mAdapter = (ProducerAdapter) in.readValue(ProducerAdapter.class.getClassLoader());
        mFragment = (ProducerFragment) in.readValue(AppCompatActivity.class.getClassLoader());
        mProducerDialog = (ProducerDialog) in.readValue(ProducerDialog.class.getClassLoader());
    }

    @Bindable
    public int getLoadingMoreVisibility() {
        return mLoadingMoreVisibility;
    }

    public void setLoadingMoreVisibility(int visibility) {
        mLoadingMoreVisibility = visibility;
        notifyPropertyChanged(BR.loadingMoreVisibility);
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
    public void setPresenter(ProducerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Bindable
    public ProducerAdapter getAdapter() {
        return mAdapter;
    }

    @Bindable
    public int getPositionScroll() {
        return mPositionScroll;
    }

    public void setPositionScroll(int positionScroll) {
        mPositionScroll = positionScroll;
        notifyPropertyChanged(BR.positionScroll);
    }

    @Override
    public void onLoadProducerSuccess(List<Producer> producers) {
        mAdapter.onUpdatePage(producers);
        mIsLoadMore = false;
        setLoadingMoreVisibility(GONE);
        setRefresh(false);
    }

    @Override
    public void onLoadProducerFailed(String msg) {
        mNavigator.showToast(msg);
        setLoadingMoreVisibility(GONE);
    }

    @Override
    public void onAddProducerFailed(String msg) {
        mNavigator.showToast(msg);
    }

    @Override
    public void onAddProducerSuccess(Producer producer) {
        mAdapter.addData(0, producer);
        setPositionScroll(0);
    }

    public void onDeleteProducerFailed(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void onDeleteProducerSuccess(Producer producer) {
        mAdapter.removeData(producer);
    }

    @Override
    public void onUpdateProducerSuccess(Producer producer, String message) {
        if (producer == null || mProducerEdit == null) {
            return;
        }
        mAdapter.onUpdateItem(mProducerEdit, producer);
        mNavigator.showToast(message);
    }

    @Override
    public void onUpdateProducerFailed(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void showProgress() {
        setLoadingMoreVisibility(VISIBLE);
    }

    @Override
    public void hideProgress() {
        setLoadingMoreVisibility(GONE);
    }

    @Override
    public void onEditProducerClick(Producer producer) {
        mProducerDialog = ProducerDialog.newInstant(producer,
            mFragment.getResources().getString(R.string.action_edit), this);
        mProducerDialog.show(mFragment.getFragmentManager(), TAG_PRODUCER_DIALOG);
    }

    @Override
    public void onDeleteProducerClick(final Producer producer) {
        new AlertDialog.Builder(mFragment.getActivity()).setTitle(
            mFragment.getString(R.string.title_delete))
            .setMessage(
                mFragment.getString(R.string.msg_delete_producer) + " " + producer.getName())
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mPresenter.deleteProducer(producer);
                }
            })
            .setNegativeButton(android.R.string.no, null)
            .create()
            .show();
    }

    @Override
    public void onAddProducerClick() {
        mProducerDialog = ProducerDialog.newInstant(new Producer(OUT_OF_INDEX, ""),
            mFragment.getResources().getString(R.string.title_add_producer), this);
        mProducerDialog.show(mFragment.getFragmentManager(), TAG_PRODUCER_DIALOG);
    }

    @Override
    public void setAllowLoadMore(boolean isAllowLoadMore) {
        mIsAllowLoadMore = isAllowLoadMore;
    }

    @Override
    public void onAddCallback(Producer producer) {
        if (producer == null) {
            return;
        }
        mPresenter.addProducer(producer);
    }

    @Override
    public void onEditCallback(Producer oldProducer, Producer newProducer) {
        if (oldProducer == null || newProducer == null) {
            return;
        }
        mProducerEdit = oldProducer;
        mPresenter.editProducer(newProducer);
    }

    @Bindable
    public RecyclerView.OnScrollListener getScrollListener() {
        return mScrollListener;
    }

    @Override
    public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
        // no ops
    }

    @Override
    public void onSearchAction(String currentQuery) {
        setKeySearch(currentQuery);
        mAdapter.clearData();
        mPresenter.getProducer(currentQuery, mGroupType.getId());
    }

    @Override
    public void onActionMenuItemSelected(FloatingSearchView searchView, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                setDrawerStatus(
                    mDrawerStatus.equals(DRAWER_IS_CLOSE) ? DRAWER_IS_OPEN : DRAWER_IS_CLOSE);
                break;
            case R.id.action_search:
                onSearchAction(searchView.getQuery());
                break;
            default:
                break;
        }
    }

    @Override
    public void onClearSearchClicked() {
        mAdapter.clearData();
        mPresenter.getProducer("", OUT_OF_INDEX);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mPresenter);
        dest.writeValue(mAdapter);
        dest.writeValue(mFragment);
        dest.writeValue(mProducerDialog);
    }

    @Bindable
    public boolean isShowCategoryFilter() {
        return mIsShowCategoryFilter;
    }

    public void setShowCategoryFilter(boolean showCategoryFilter) {
        mIsShowCategoryFilter = showCategoryFilter;
        notifyPropertyChanged(BR.showCategoryFilter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        Bundle bundle = data.getExtras();
        Status status = bundle.getParcelable(BUNDLE_DATA);
        if (status != null) {
            setGroupType((Producer) status);
        }
        setDrawerStatus(DRAWER_IS_CLOSE);
    }

    @Bindable
    public String getDrawerStatus() {
        return mDrawerStatus;
    }

    public void setDrawerStatus(String drawerStatus) {
        mDrawerStatus = drawerStatus;
        notifyPropertyChanged(BR.drawerStatus);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        // no ops
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        setDrawerStatus(DRAWER_IS_OPEN);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        setDrawerStatus(DRAWER_IS_CLOSE);
        mAdapter.clearData();
        mPresenter.getProducer(getKeySearch(), mGroupType.getId());
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        // no ops
    }

    @Bindable
    public Producer getGroupType() {
        return mGroupType;
    }

    public void setGroupType(Producer groupType) {
        mGroupType = groupType;
        notifyPropertyChanged(BR.groupType);
    }

    public void onChooseGroupTypeClick() {
        mFragment.startActivityForResult(SelectionActivity.getInstance(mContext, DEVICE_GROUP),
            REQUEST_DEVICE_GROUPS);
    }

    public void onClearFilterClick() {
        mAdapter.clearData();
        mGroupType = new Producer(OUT_OF_INDEX, Constant.DeviceUsingStatus.ALL);
        setDrawerStatus(DRAWER_IS_CLOSE);
    }

    @Bindable
    public String getKeySearch() {
        return mKeySearch;
    }

    public void setKeySearch(String keySearch) {
        mKeySearch = keySearch;
        notifyPropertyChanged(BR.keySearch);
    }

    @Bindable
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
}
