package com.framgia.fdms.screen.device.listdevice;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.screen.assignment.AssignmentActivity;
import com.framgia.fdms.screen.assignment.AssignmentType;
import com.framgia.fdms.screen.devicecreation.CreateDeviceActivity;
import com.framgia.fdms.screen.devicecreation.DeviceStatusType;
import com.framgia.fdms.screen.devicedetail.DeviceDetailActivity;
import com.framgia.fdms.screen.returndevice.ReturnDeviceActivity;
import com.framgia.fdms.screen.selection.SelectionActivity;
import com.framgia.fdms.utils.navigator.Navigator;
import com.framgia.fdms.widget.OnSearchMenuItemClickListener;
import com.github.clans.fab.FloatingActionMenu;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.VISIBLE;
import static com.framgia.fdms.screen.selection.SelectionType.CATEGORY;
import static com.framgia.fdms.screen.selection.SelectionType.MARKER;
import static com.framgia.fdms.screen.selection.SelectionType.MEETING_ROOM;
import static com.framgia.fdms.screen.selection.SelectionType.STATUS;
import static com.framgia.fdms.screen.selection.SelectionType.VENDOR;
import static com.framgia.fdms.screen.selection.SelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_SUCCESS;
import static com.framgia.fdms.utils.Constant.DRAWER_IS_CLOSE;
import static com.framgia.fdms.utils.Constant.DRAWER_IS_OPEN;
import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_CATEGORY;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_CREATE_ASSIGNMENT;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_MAKER;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_MEETING_ROOM;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_STATUS;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_VENDOR;

/**
 * Exposes the data to be used in the ListDevice screen.
 */
public class ListDeviceViewModel extends BaseObservable
    implements ListDeviceContract.ViewModel, ItemDeviceClickListenner,
    FloatingSearchView.OnSearchListener, FloatingSearchView.OnClearSearchActionListener,
    OnSearchMenuItemClickListener, DrawerLayout.DrawerListener {

    private ListDeviceFragment mFragment;
    private boolean mIsLoadingMore;
    private boolean mIsAllowLoadMore = true;
    private ListDeviceAdapter mAdapter;
    private ListDeviceContract.Presenter mPresenter;
    private Context mContext;

    private boolean mIsBo;
    private int mEmptyViewVisible = View.GONE;

    private boolean mIsRefresh;
    private String mDrawerStatus = DRAWER_IS_CLOSE;
    private DeviceFilterModel mFilterModel;
    private boolean mIsChangeFilter;
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
            if (mIsAllowLoadMore
                && !mIsLoadingMore
                && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                setLoadingMore(true);
                mPresenter.loadMoreData();
            }
        }
    };

    private SearchView.OnQueryTextListener mQueryTextListener =
        new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                mIsChangeFilter = true;
                mFilterModel.setDeviceName(query);
                setDrawerStatus(DRAWER_IS_CLOSE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mIsChangeFilter = true;
                mFilterModel.setDeviceName(newText);
                return false;
            }
        };

    public ListDeviceViewModel(ListDeviceFragment fragment) {
        mFragment = fragment;
        mContext = fragment.getContext();
        mAdapter = new ListDeviceAdapter(mContext, new ArrayList<Device>(), this);
        mFilterModel = new DeviceFilterModel();
        mNavigator = new Navigator(fragment);
    }

    public void loadData() {
        mPresenter.getData(mFilterModel, FIRST_PAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        if (resultCode != RESULT_OK) {
            return;
        }
        mIsChangeFilter = true;
        Bundle bundle = dataIntent.getExtras();
        Status data = bundle.getParcelable(BUNDLE_DATA);
        switch (requestCode) {
            case REQUEST_CATEGORY:
                mFilterModel.setCategory(data);
                break;
            case REQUEST_STATUS:
                mFilterModel.setStatus(data);
                break;
            case REQUEST_MAKER:
                mFilterModel.setMarker((Producer) data);
                break;
            case REQUEST_VENDOR:
                mFilterModel.setVendor((Producer) data);
                break;
            case REQUEST_MEETING_ROOM:
                mFilterModel.setMeetingRoom((Producer) data);
                break;
            case REQUEST_CREATE_ASSIGNMENT:
                mNavigator.showToast(bundle.getInt(BUNDLE_SUCCESS));
                mAdapter.clear();
                mPresenter.getData(mFilterModel, FIRST_PAGE);
                break;
            default:
                break;
        }
        setDrawerStatus(DRAWER_IS_CLOSE);
    }

    @Override
    public void setupFloatingActionsMenu(User user) {
        String role = user.getRole();
        if (role == null) {
            return;
        }
        setBo(user.isBo());
    }

    @Override
    public void onChooseCategoryClick() {
        mFragment.startActivityForResult(SelectionActivity.getInstance(mContext, CATEGORY),
            REQUEST_CATEGORY);
    }

    @Override
    public void onChooseStatusClick() {
        mFragment.startActivityForResult(SelectionActivity.getInstance(mContext, STATUS),
            REQUEST_STATUS);
    }

    @Override
    public void onChooseMakerClick() {
        mFragment.startActivityForResult(SelectionActivity.getInstance(mContext, MARKER),
            REQUEST_MAKER);
    }

    @Override
    public void onChooseVendorClick() {
        mFragment.startActivityForResult(SelectionActivity.getInstance(mContext, VENDOR),
            REQUEST_VENDOR);
    }

    @Override
    public void onChooseMeetingRoomClick() {
        mFragment.startActivityForResult(SelectionActivity.getInstance(mContext, MEETING_ROOM),
            REQUEST_MEETING_ROOM);
    }

    @Override
    public void onClearFilterClick() {
        mFilterModel.initDefaultFilter();
        mIsChangeFilter = true;
        setDrawerStatus(DRAWER_IS_CLOSE);
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
    public void setPresenter(ListDeviceContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDeviceLoaded(List<Device> devices) {
        setEmptyViewVisible(
            devices.isEmpty() && mAdapter.getItemCount() == 0 ? VISIBLE : View.GONE);
        setLoadingMore(false);
        mAdapter.onUpdatePage(devices);
        setRefresh(false);
    }

    @Override
    public void showProgressbar() {
        setLoadingMore(true);
    }

    @Override
    public void onError(String msg) {
        setLoadingMore(false);
        hideProgressbar();
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        setRefresh(false);
        if (mAdapter.getItemCount() == 0) {
            setEmptyViewVisible(VISIBLE);
        }
    }

    @Override
    public void hideProgressbar() {
        setLoadingMore(false);
    }

    @Override
    public void onStartReturnDevice(FloatingActionMenu floatingActionsMenu) {
        floatingActionsMenu.close(true);
        mFragment.startActivity(ReturnDeviceActivity.newIntent(mFragment.getContext()));
    }

    @Override
    public void onRegisterDeviceClick(FloatingActionMenu floatingActionsMenu) {
        floatingActionsMenu.close(true);
        mFragment.startActivity(
            CreateDeviceActivity.getInstance(mFragment.getContext(), DeviceStatusType.CREATE));
    }

    @Override
    public void onAssignDeviceForNewMemberClick(FloatingActionMenu floatingActionsMenu) {
        floatingActionsMenu.close(true);
        mFragment.startActivityForResult(AssignmentActivity.getInstance(mFragment.getContext(),
            AssignmentType.ASSIGN_BY_NEW_MEMBER), REQUEST_CREATE_ASSIGNMENT);
    }

    @Override
    public void getDataWithDevice(Device device) {
        if (device == null
            || device.getDeviceCategoryId() <= 0
            || device.getDeviceCategoryName() == null) {
            return;
        }
        mFilterModel.initDefaultFilter();
        mFilterModel.setCategory(
            new Status(device.getDeviceCategoryId(), device.getDeviceCategoryName()));
        mAdapter.clear();
        mPresenter.getData(mFilterModel, FIRST_PAGE);
    }

    @Override
    public void onStartGetData() {
        mIsChangeFilter = false;
    }

    @Override
    public void setAllowLoadMore(boolean allowLoadMore) {
        mIsAllowLoadMore = allowLoadMore;
    }

    public RecyclerView.OnScrollListener getScrollListenner() {
        return mScrollListenner;
    }

    @Bindable
    public ListDeviceAdapter getAdapter() {
        return mAdapter;
    }

    @Bindable
    public boolean isBo() {
        return mIsBo;
    }

    public void setBo(boolean bo) {
        mIsBo = bo;
        notifyPropertyChanged(BR.bo);
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

    public ListDeviceFragment getFragment() {
        return mFragment;
    }

    public void setFragment(ListDeviceFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public void onItemDeviceClick(Device device) {
        mContext.startActivity(DeviceDetailActivity.getInstance(mContext, device));
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
    public void onClearSearchClicked() {
        mAdapter.clear();
        mFilterModel.setStaffName("");
        mPresenter.getData(mFilterModel, FIRST_PAGE);
    }

    @Override
    public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
        // no ops
    }

    @Override
    public void onSearchAction(String currentQuery) {
        mAdapter.clear();
        mFilterModel.setStaffName(currentQuery);
        mPresenter.getData(mFilterModel, FIRST_PAGE);
    }

    @Override
    public void onActionMenuItemSelected(FloatingSearchView searchView, MenuItem item) {
        // TODO: 9/14/17
        switch (item.getItemId()) {
            case R.id.action_filter:
                setDrawerStatus(
                    mDrawerStatus == DRAWER_IS_CLOSE ? DRAWER_IS_OPEN : DRAWER_IS_CLOSE);
                break;
            case R.id.action_search:
                mAdapter.clear();
                mFilterModel.setStaffName(searchView.getQuery());
                mPresenter.getData(mFilterModel, FIRST_PAGE);
                break;
            default:
                break;
        }
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
        if (!mIsChangeFilter) {
            return;
        }
        setDrawerStatus(DRAWER_IS_CLOSE);
        mAdapter.clear();
        mPresenter.getData(mFilterModel, FIRST_PAGE);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        // no ops
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
    public DeviceFilterModel getFilterModel() {
        return mFilterModel;
    }

    public void setFilterModel(DeviceFilterModel filterModel) {
        mFilterModel = filterModel;
        notifyPropertyChanged(BR.filterModel);
    }

    @Bindable
    public SearchView.OnQueryTextListener getQueryTextListener() {
        return mQueryTextListener;
    }
}
