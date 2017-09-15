package com.framgia.fdms.screen.device.listdevice;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.framgia.fdms.screen.devicecreation.CreateDeviceActivity;
import com.framgia.fdms.screen.devicecreation.DeviceStatusType;
import com.framgia.fdms.screen.devicedetail.DeviceDetailActivity;
import com.framgia.fdms.screen.new_selection.StatusSelectionActivity;
import com.framgia.fdms.screen.returndevice.ReturnDeviceActivity;
import com.framgia.fdms.widget.OnSearchMenuItemClickListener;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.VISIBLE;
import static com.framgia.fdms.screen.device.DeviceViewModel.Tab.TAB_MANAGE_DEVICE;
import static com.framgia.fdms.screen.device.DeviceViewModel.Tab.TAB_MY_DEVICE;
import static com.framgia.fdms.screen.new_selection.SelectionType.CATEGORY;
import static com.framgia.fdms.screen.new_selection.SelectionType.MARKER;
import static com.framgia.fdms.screen.new_selection.SelectionType.MEETING_ROOM;
import static com.framgia.fdms.screen.new_selection.SelectionType.STATUS;
import static com.framgia.fdms.screen.new_selection.SelectionType.VENDOR;
import static com.framgia.fdms.screen.new_selection.StatusSelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.DRAWER_IS_CLOSE;
import static com.framgia.fdms.utils.Constant.DRAWER_IS_OPEN;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_CATEGORY;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_MAKER;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_MEETING_ROOM;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_STATUS;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_VENDOR;
import static com.framgia.fdms.utils.Constant.TITLE_NA;

/**
 * Exposes the data to be used in the ListDevice screen.
 */
public class ListDeviceViewModel extends BaseObservable
    implements ListDeviceContract.ViewModel, ItemDeviceClickListenner,
    FloatingSearchView.OnSearchListener, FloatingSearchView.OnClearSearchActionListener,
    OnSearchMenuItemClickListener, DrawerLayout.DrawerListener {
    private ListDeviceFragment mFragment;
    private int mProgressBarVisibility;
    private boolean mIsLoadingMore;
    private ListDeviceAdapter mAdapter;
    private ListDeviceContract.Presenter mPresenter;
    private Context mContext;

    private String mKeyWord;
    private boolean mIsBo;
    private int mTab = TAB_MY_DEVICE;
    private int mEmptyViewVisible = View.GONE;

    private boolean mIsTopSheetExpand;
    private boolean mIsRefresh;
    private String mDrawerStatus = DRAWER_IS_CLOSE;

    private Status mStatus;
    private Status mCategory;
    private Producer mVendor, mMarker;
    private Producer mMeetingRoom;
    private String mDeviceName;
    private String mStaffName;

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

    public ListDeviceViewModel(ListDeviceFragment fragment, int tabDevice) {
        mFragment = fragment;
        mContext = fragment.getContext();
        mAdapter = new ListDeviceAdapter(mContext, new ArrayList<Device>(), this);
        initDeaultFilter();
        mTab = tabDevice;
    }

    private void initDeaultFilter() {
        setCategory(new Status(OUT_OF_INDEX, TITLE_NA));
        setStatus(new Status(OUT_OF_INDEX, TITLE_NA));
        setMeetingRoom(new Producer(OUT_OF_INDEX, TITLE_NA));
        setVendor(new Producer(OUT_OF_INDEX, TITLE_NA));
        setMarker(new Producer(OUT_OF_INDEX, TITLE_NA));
        setDeviceName("");
        setStaffName("");
    }

    public void loadData() {
        if (mPresenter == null) return;
        switch (mTab) {
            case TAB_MY_DEVICE:
                mPresenter.getDevicesBorrow();
                break;
            case TAB_MANAGE_DEVICE:
                mPresenter.getData(mKeyWord, mCategory, mStatus);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        if (resultCode != RESULT_OK) {
            return;
        }
        Bundle bundle = dataIntent.getExtras();
        Status data = bundle.getParcelable(BUNDLE_DATA);
        switch (requestCode) {
            case REQUEST_CATEGORY:
                setCategory(data);
                break;
            case REQUEST_STATUS:
                setStatus(data);
                break;
            case REQUEST_MAKER:
                setMarker((Producer) data);
                break;
            case REQUEST_VENDOR:
                setVendor((Producer) data);
                break;
            case REQUEST_MEETING_ROOM:
                setMeetingRoom((Producer) data);
                break;
            default:
                break;
        }
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
        mFragment.startActivityForResult(StatusSelectionActivity.getInstance(mContext, CATEGORY),
            REQUEST_CATEGORY);
    }

    @Override
    public void onChooseStatusClick() {
        mFragment.startActivityForResult(StatusSelectionActivity.getInstance(mContext, STATUS),
            REQUEST_STATUS);
    }

    @Override
    public void onChooseMakerClick() {
        mFragment.startActivityForResult(StatusSelectionActivity.getInstance(mContext, MARKER),
            REQUEST_MAKER);
    }

    @Override
    public void onChooseVendorClick() {
        mFragment.startActivityForResult(StatusSelectionActivity.getInstance(mContext, VENDOR),
            REQUEST_VENDOR);
    }

    @Override
    public void onChooseMeetingRoomClick() {
        mFragment.startActivityForResult(
            StatusSelectionActivity.getInstance(mContext, MEETING_ROOM), REQUEST_MEETING_ROOM);
    }

    @Override
    public void onReset() {
        mAdapter.clear();
        mPresenter.getData(null, mCategory, mStatus);
    }

    @Override
    public void getData() {
        mPresenter.getData(null, null, null);
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
        setProgressBarVisibility(VISIBLE);
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
        setProgressBarVisibility(View.GONE);
    }

    @Override
    public void onSearch(String keyWord) {
        mAdapter.clear();
        mKeyWord = keyWord;
        mPresenter.getData(mKeyWord, mCategory, mStatus);
    }

    @Override
    public void onStartReturnDevice(FloatingActionsMenu floatingActionsMenu) {
        floatingActionsMenu.collapse();
        mFragment.startActivity(ReturnDeviceActivity.newIntent(mFragment.getContext()));
    }

    @Override
    public void onRegisterDeviceClick(FloatingActionsMenu floatingActionsMenu) {
        floatingActionsMenu.collapse();
        mFragment.startActivity(
            CreateDeviceActivity.getInstance(mFragment.getContext(), DeviceStatusType.CREATE));
    }

    @Override
    public void getDataWithDevice(Device device) {
        if (device == null
            || device.getDeviceCategoryId() <= 0
            || device.getDeviceCategoryName() == null) {
            return;
        }
        setCategory(new Status(device.getDeviceCategoryId(), device.getDeviceCategoryName()));
        mAdapter.clear();
        mPresenter.getData(mKeyWord, mCategory, mStatus);
    }

    public RecyclerView.OnScrollListener getScrollListenner() {
        return mScrollListenner;
    }

    @Bindable
    public ListDeviceAdapter getAdapter() {
        return mAdapter;
    }

    @Bindable
    public Status getCategory() {
        return mCategory;
    }

    public void setCategory(Status category) {
        mCategory = category;
        notifyPropertyChanged(BR.category);
    }

    @Bindable
    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
        notifyPropertyChanged(BR.status);
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
    public int getTab() {
        return mTab;
    }

    public void setTab(int tab) {
        mTab = tab;
        notifyPropertyChanged(BR.tab);
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

    @Bindable
    public Producer getVendor() {
        return mVendor;
    }

    public void setVendor(Producer vendor) {
        mVendor = vendor;
        notifyPropertyChanged(BR.vendor);
    }

    @Bindable
    public Producer getMarker() {
        return mMarker;
    }

    public void setMarker(Producer marker) {
        mMarker = marker;
        notifyPropertyChanged(BR.marker);
    }

    @Bindable
    public boolean isTopSheetExpand() {
        return mIsTopSheetExpand;
    }

    public void setTopSheetExpand(boolean topSheetExpand) {
        mIsTopSheetExpand = topSheetExpand;
        notifyPropertyChanged(BR.topSheetExpand);
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
        // TODO: 9/14/17  
    }

    @Override
    public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
        // TODO: 9/14/17  
    }

    @Override
    public void onSearchAction(String currentQuery) {
        // TODO: 9/14/17  
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
                // TODO: 9/14/17
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
        setDrawerStatus(DRAWER_IS_CLOSE);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        // no ops
    }

    @Bindable
    public Producer getMeetingRoom() {
        return mMeetingRoom;
    }

    public void setMeetingRoom(Producer meetingRoom) {
        mMeetingRoom = meetingRoom;
        notifyPropertyChanged(BR.meetingRoom);
    }

    @Bindable
    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        mDeviceName = deviceName;
        notifyPropertyChanged(BR.deviceName);
    }

    @Bindable
    public String getStaffName() {
        return mStaffName;
    }

    public void setStaffName(String staffName) {
        mStaffName = staffName;
        notifyPropertyChanged(BR.staffName);
    }

    @Bindable
    public int getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public void setProgressBarVisibility(int progressBarVisibility) {
        mProgressBarVisibility = progressBarVisibility;
        notifyPropertyChanged(BR.progressBarVisibility);
    }

    @Bindable
    public boolean isLoadingMore() {
        return mIsLoadingMore;
    }

    public void setLoadingMore(boolean loadingMore) {
        mIsLoadingMore = loadingMore;
        notifyPropertyChanged(BR.loadingMore);
    }
}
