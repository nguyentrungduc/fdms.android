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
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.FDMSApplication;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.screen.meetingroomdetail.DetailMeetingRoomActivity;
import com.framgia.fdms.screen.selection.SelectionActivity;
import com.framgia.fdms.screen.selection.SelectionType;
import com.framgia.fdms.utils.Constant;
import com.framgia.fdms.utils.navigator.Navigator;
import com.framgia.fdms.widget.OnSearchMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.framgia.fdms.screen.producer.ProducerDialog.Type.ADD;
import static com.framgia.fdms.screen.producer.ProducerDialog.Type.EDIT;
import static com.framgia.fdms.screen.selection.SelectionType.BRANCH_ALL;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_GROUP_ALL;
import static com.framgia.fdms.screen.selection.SelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.DRAWER_IS_CLOSE;
import static com.framgia.fdms.utils.Constant.DRAWER_IS_OPEN;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_BRANCH;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_DEVICE_GROUPS;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_DEVICE_GROUPS_DIALOG;
import static com.framgia.fdms.utils.Constant.TAG_PRODUCER_DIALOG;

/**
 * Exposes the data to be used in the Producer screen.
 */
public class ProducerViewModel extends BaseObservable implements ProducerContract.ViewModel,
        ProducerDialogContract.ActionCallback, FloatingSearchView.OnSearchListener,
        FloatingSearchView.OnClearSearchActionListener, OnSearchMenuItemClickListener,
        BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Producer>, DrawerLayout
                .DrawerListener {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProducerViewModel> CREATOR = new Parcelable
            .Creator<ProducerViewModel>() {
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
    private boolean mIsShowFilter;
    private boolean mIsShowBranchFilter;
    private boolean mIsShowGroupDevice;
    private String mDrawerStatus = DRAWER_IS_CLOSE;
    private Producer mGroupType, mCategory, mTempGroupType;
    private Status mBranch;
    private Context mContext;
    private String mKeySearch;
    private boolean mIsRefresh;
    @ProducerType
    private int mProducerType;

    private String mSearchHint;
    private int mEmptyViewVisible = GONE;
    private String mEmptyViewMessage;

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy <= 0) {
                return;
            }
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
            if (mIsAllowLoadMore && !mIsLoadMore && (visibleItemCount + pastVisiblesItems) >=
                    totalItemCount) {
                mIsLoadMore = true;
                setLoadingMoreVisibility(View.VISIBLE);
                mPresenter.loadMorePage();
            }
        }
    };

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout
            .OnRefreshListener() {
        @Override
        public void onRefresh() {
            mAdapter.clearData();
            mPresenter.getProducer(mKeySearch, mGroupType.getId(), mBranch.getId());
        }
    };

    public ProducerViewModel(ProducerFragment producerFragment, @ProducerType int producerType) {
        mFragment = producerFragment;
        mProducerType = producerType;
        mContext = producerFragment.getContext();
        mNavigator = new Navigator(mFragment);
        mAdapter = new ProducerAdapter(FDMSApplication.getInstant(), this, new
                ArrayList<Producer>());
        setLoadingMoreVisibility(GONE);
        mGroupType = new Producer(OUT_OF_INDEX, Constant.DeviceUsingStatus.ALL);
        mBranch = new Producer(OUT_OF_INDEX, Constant.DeviceUsingStatus.ALL);
        initHintSearch();
    }

    protected ProducerViewModel(Parcel in) {
        mPresenter = (ProducerContract.Presenter) in.readValue(ProducerContract.Presenter.class
                .getClassLoader());
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
        initEmptyView(mContext.getString(R.string.error_filter_empty));
    }

    @Override
    public void onLoadProducerFailed(String msg) {
        mNavigator.showToast(msg);
        setLoadingMoreVisibility(GONE);
        initEmptyView(msg);
    }

    private void initEmptyView(String msg) {
        if (mAdapter.getItemCount() != 0) {
            setEmptyViewVisible(GONE);
            setEmptyViewMessage(msg);
        } else {
            setEmptyViewVisible(VISIBLE);
        }
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
        if (getGroupType().getId() == OUT_OF_INDEX) {
            mTempGroupType = new Producer(1, mFragment.getResources().getString(R.string
                    .title_computer_device));
        } else {
            mTempGroupType = getGroupType();
        }
        mProducerDialog = ProducerDialog.newInstant(producer, mProducerType, EDIT, this,
                mTempGroupType, isShowGroupDevice());
        mProducerDialog.show(mFragment.getFragmentManager(), TAG_PRODUCER_DIALOG);
    }

    @Override
    public void onDeleteProducerClick(final Producer producer) {
        new AlertDialog.Builder(mFragment.getActivity()).setTitle(mFragment.getString(R.string
                .title_delete)).setMessage(mFragment.getString(R.string.msg_delete_producer) + " " +
                "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" +
                "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" + "" +
                producer.getName()).setPositiveButton(android.R.string.yes, new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.deleteProducer(producer);
            }
        }).setNegativeButton(android.R.string.no, null).create().show();
    }

    private void initHintSearch() {
        switch (mProducerType) {
            case ProducerType.VENDOR:
                setSearchHint(mContext.getString(R.string.hint_search_vendor));
                break;
            case ProducerType.MARKER:
                setSearchHint(mContext.getString(R.string.hint_search_maker));
                break;
            case ProducerType.MEETING_ROOM:
                setSearchHint(mContext.getString(R.string.hint_search_meeting_room));
                break;
            case ProducerType.DEVICE_GROUPS:
                setSearchHint(mContext.getString(R.string.hint_search_group_name));
                break;
            case ProducerType.CATEGORIES_GROUPS:
                setSearchHint(mContext.getString(R.string.hint_search_device_category_name));
                break;
            default:
                break;
        }
    }

    @Override
    public void onAddProducerClick() {
        mTempGroupType = new Producer(1, mFragment.getResources().getString(R.string
                .title_computer_device));
        mProducerDialog = ProducerDialog.newInstant(new Producer(OUT_OF_INDEX, ""),
                mProducerType, ADD, this, getTempGroupType(), isShowGroupDevice());
        mProducerDialog.show(mFragment.getFragmentManager(), TAG_PRODUCER_DIALOG);
    }

    @Override
    public void setAllowLoadMore(boolean isAllowLoadMore) {
        mIsAllowLoadMore = isAllowLoadMore;
    }

    @Override
    public void onAddCallback(Producer producer, Producer tempGroupDevice) {
        if (producer == null) {
            return;
        }
        mPresenter.addProducer(producer, tempGroupDevice);
    }

    @Override
    public void onEditCallback(Producer oldProducer, Producer newProducer, Producer tempGroupType) {
        if (oldProducer == null || newProducer == null) {
            return;
        }
        mProducerEdit = oldProducer;
        mPresenter.editProducer(newProducer, tempGroupType);
    }

    @Override
    public void onChooseGroupTypeClickDialog(Producer tempDeviceCategory, Producer tempGroupType) {
        mCategory = tempDeviceCategory;
        mTempGroupType = tempGroupType;
        mFragment.startActivityForResult(SelectionActivity.getInstance(mContext, SelectionType
                .DEVICE_GROUP_DIALOG), REQUEST_DEVICE_GROUPS_DIALOG);
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
        mPresenter.getProducer(currentQuery, mGroupType.getId(), mBranch.getId());
    }

    @Override
    public void onActionMenuItemSelected(FloatingSearchView searchView, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                setDrawerStatus(mDrawerStatus.equals(DRAWER_IS_CLOSE) ? DRAWER_IS_OPEN :
                        DRAWER_IS_CLOSE);
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
        mPresenter.getProducer("", OUT_OF_INDEX, OUT_OF_INDEX);
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
    public boolean isShowFilter() {
        return mIsShowFilter;
    }

    public void setShowFilter(boolean showFilter) {
        mIsShowFilter = showFilter;
        notifyPropertyChanged(BR.showFilter);
    }

    @Bindable
    public boolean isShowBranchFilter() {
        return mIsShowBranchFilter;
    }

    public void setShowBranchFilter(boolean showBranchFilter) {
        mIsShowBranchFilter = showBranchFilter;
        notifyPropertyChanged(BR.showBranchFilter);
    }

    @Bindable
    public boolean isShowGroupDevice() {
        return mIsShowGroupDevice;
    }

    public void setShowGroupDevice(boolean showGroupDevice) {
        mIsShowGroupDevice = showGroupDevice;
        notifyPropertyChanged(BR.showGroupDevice);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        Bundle bundle = data.getExtras();
        Status status = bundle.getParcelable(BUNDLE_DATA);
        switch (requestCode) {
            case REQUEST_DEVICE_GROUPS:
                setGroupType((Producer) status);
                break;
            case REQUEST_BRANCH:
                setBranch(status);
                break;
            case REQUEST_DEVICE_GROUPS_DIALOG:
                setTempGroupType((Producer) status);
                mProducerDialog = ProducerDialog.newInstant(mCategory, mProducerType, ADD, this,
                        mTempGroupType, isShowFilter());
                mProducerDialog.show(mFragment.getFragmentManager(), TAG_PRODUCER_DIALOG);
                break;
            default:
                break;
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
        mPresenter.getProducer(getKeySearch(), mGroupType.getId(), mBranch.getId());
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

    @Bindable
    public Status getBranch() {
        return mBranch;
    }

    public void setBranch(Status branch) {
        mBranch = branch;
        notifyPropertyChanged(BR.branch);
    }

    public void onChooseGroupTypeClick() {
        mFragment.startActivityForResult(SelectionActivity.getInstance(mContext,
                DEVICE_GROUP_ALL), REQUEST_DEVICE_GROUPS);
    }

    public void onChooseBranchClick() {
        mFragment.startActivityForResult(SelectionActivity.getInstance(mContext, BRANCH_ALL),
                REQUEST_BRANCH);
    }

    public void onClearFilterClick() {
        mAdapter.clearData();
        setGroupType(new Producer(OUT_OF_INDEX, Constant.DeviceUsingStatus.ALL));
        setBranch(new Producer(OUT_OF_INDEX, Constant.DeviceUsingStatus.ALL));
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

    @Bindable
    public Producer getTempGroupType() {
        return mTempGroupType;
    }

    public void setTempGroupType(Producer tempGroupType) {
        mTempGroupType = tempGroupType;
        notifyPropertyChanged(BR.tempGroupType);
    }

    @Bindable
    public String getSearchHint() {
        return mSearchHint;
    }

    public void setSearchHint(String searchHint) {
        mSearchHint = searchHint;
        notifyPropertyChanged(BR.searchHint);
    }

    @Override
    public void onItemRecyclerViewClick(Producer item) {
        if (isShowBranchFilter()) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constant.BundleConstant.BUNDLE_MEETING_ROOM, item);
            mNavigator.startActivity(DetailMeetingRoomActivity.class, bundle);
        }
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
    public String getEmptyViewMessage() {
        return mEmptyViewMessage;
    }

    public void setEmptyViewMessage(String emptyViewMessage) {
        mEmptyViewMessage = emptyViewMessage;
        notifyPropertyChanged(BR.emptyViewMessage);
    }
}
