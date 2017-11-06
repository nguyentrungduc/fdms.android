package com.framgia.fdms.screen.dashboard.dashboarddetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Dashboard;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.screen.device.OnDeviceClickListenner;
import com.framgia.fdms.screen.request.OnRequestClickListenner;
import com.framgia.fdms.screen.request.userrequest.UserRequestAdapter;
import com.framgia.fdms.screen.requestdetail.RequestDetailActivity;
import com.framgia.fdms.utils.navigator.Navigator;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fdms.screen.dashboard.dashboarddetail.DashBoardDetailFragment
        .DEVICE_DASHBOARD;
import static com.framgia.fdms.screen.dashboard.dashboarddetail.DashBoardDetailFragment
        .REQUEST_DASHBOARD;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_RESPONE;
import static com.framgia.fdms.utils.Constant.MANAGE_REQUEST_GROUP;
import static com.framgia.fdms.utils.Constant.RequestAction.CANCEL;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_DETAIL;

/**
 * Exposes the data to be used in the Scanner screen.
 */
public class DashBoardDetailViewModel extends BaseObservable
        implements DashBoardDetailContract.ViewModel, OnRequestClickListenner, OnDeviceClickListenner {
    /**
     * Listener Event DashBoardDetail
     */
    public interface OnDashBoardDetailClickListener {
        void onItemClick(Device device);
    }

    private static final float PIE_DATA_SLICE_SPACE = 3f;
    private static final float PIE_DATA_SLECTION_SHIFT = 5f;
    private DashBoardDetailContract.Presenter mPresenter;
    private PieData mPieData;
    private Context mContext;
    private int mTotal;
    private UserRequestAdapter mAdapterTopRequest;
    private TopDeviceAdapter mAdapterTopDevice;
    private String mDashboardTitle;
    private int mEmptyViewVisible = View.GONE;
    private int mDashboardType;
    private Fragment mFragment;
    private boolean mIsRefresh;
    private Navigator mNavigator;
    private SwipeRefreshLayout.OnRefreshListener mRefreshLayout =
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mAdapterTopDevice.clear();
                    mAdapterTopRequest.clear();
                    getData();
                }
            };

    public DashBoardDetailViewModel(Fragment fragment, int dashboardType) {
        mFragment = fragment;
        mContext = fragment.getContext();
        mNavigator = new Navigator(fragment);
        mPieData = new PieData();
        mAdapterTopRequest =
                new UserRequestAdapter(mContext, new ArrayList<Request>(), this, new User());
        mAdapterTopDevice = new TopDeviceAdapter(mContext, this, new ArrayList<Device>());
        initDashboardTitle(dashboardType);
        mDashboardType = dashboardType;
    }

    private void initDashboardTitle(int dashboardType) {
        if (dashboardType == DEVICE_DASHBOARD) {
            setDashboardTitle(mContext.getString(R.string.title_top_devices));
        } else if (dashboardType == REQUEST_DASHBOARD) {
            setDashboardTitle(mContext.getString(R.string.title_top_requests));
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
    public void setPresenter(DashBoardDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || data.getExtras() == null || resultCode != RESULT_OK) {
            return;
        }
        Bundle bundle = data.getExtras();
        if (requestCode == REQUEST_DETAIL) {
            Request request =
                    (Request) bundle.getSerializable(BUNDLE_RESPONE);
            if (request != null) {
                onUpdateActionSuccess(request);
            }
        }
    }

    @Override
    public void setCurrentUser(User user) {
        if (user == null) return;
        mAdapterTopRequest.updateUser(user);
    }

    @Bindable
    public PieData getPieData() {
        return mPieData;
    }

    public void setPieData(PieData pieData) {
        mPieData = pieData;
        notifyPropertyChanged(BR.pieData);
    }

    @Override
    public void setDataSet(PieDataSet dataSet) {
        mPieData = new PieData(dataSet);
        setPieData(mPieData);
    }

    @Override
    public void onDashBoardError(String error) {
        Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
        setEmptyViewVisible(View.VISIBLE);
    }

    @Override
    public void onDashBoardLoaded(List<Dashboard> dashboards) {
        int total = 0;
        List<Integer> colors = new ArrayList<Integer>();
        List<PieEntry> values = new ArrayList<PieEntry>();
        if (dashboards != null && dashboards.size() != 0) {
            setEmptyViewVisible(View.GONE);
        } else {
            setEmptyViewVisible(View.VISIBLE);
        }
        for (Dashboard dashboard : dashboards) {
            total += dashboard.getCount();
        }
        for (int i = 0; i < dashboards.size(); i++) {
            Dashboard dashboard = dashboards.get(i);
            values.add(new PieEntry(dashboard.getCount(), dashboard.getTitle(), i));
            colors.add(Color.parseColor(dashboard.getBackgroundColor()));
        }
        PieDataSet dataSet = new PieDataSet(values, mContext.getString(R.string.title_chart));
        dataSet.setSliceSpace(PIE_DATA_SLICE_SPACE);
        dataSet.setSelectionShift(PIE_DATA_SLECTION_SHIFT);
        dataSet.setColors(colors);
        setDataSet(dataSet);
        setTotal(total);
    }

    @Override
    public void getData() {
        mPresenter.getData();
    }

    @Override
    public void onGetTopRequestSuccess(List<Request> requests) {
        if (requests == null) return;
        mAdapterTopRequest.onUpdatePage(requests);
    }

    @Override
    public void onGetTopDeviceSuccess(List<Device> devices) {
        if (devices == null) return;
        mAdapterTopDevice.onUpdatePage(devices);
    }

    @Override
    public void onUpdateActionSuccess(Request request) {
        if (request == null) {
            return;
        }
        mAdapterTopRequest.updateItem(request);
    }

    @Override
    public void showProgressbar() {
        // TODO: 08/06/2017
    }

    @Override
    public void hideProgressbar() {
        // TODO: 08/06/2017
    }

    @Bindable
    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
        notifyPropertyChanged(BR.total);
    }

    public UserRequestAdapter getAdapterTopRequest() {
        return mAdapterTopRequest;
    }

    public TopDeviceAdapter getAdapterTopDevice() {
        return mAdapterTopDevice;
    }

    @Bindable
    public String getDashboardTitle() {
        return mDashboardTitle;
    }

    public void setDashboardTitle(String dashboardTitle) {
        mDashboardTitle = dashboardTitle;
        notifyPropertyChanged(BR.dashboardTitle);
    }

    @Bindable
    public int getEmptyViewVisible() {
        return mEmptyViewVisible;
    }

    public void setEmptyViewVisible(int emptyViewVisible) {
        mEmptyViewVisible = emptyViewVisible;
        notifyPropertyChanged(BR.emptyViewVisible);
    }

    public int getDashboardType() {
        return mDashboardType;
    }

    @Override
    public void onMenuClick(View v, final UserRequestAdapter.RequestModel request) {
        if (request == null
                || request.getRequest() == null
                || request.getRequest().getRequestActionList() == null) {
            return;
        }
        Request requestModel = request.getRequest();
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        for (int i = 0; i < requestModel.getRequestActionList().size(); i++) {
            final Request.RequestAction action = requestModel.getRequestActionList().get(i);
            popupMenu.getMenu()
                    .add(action.getName())
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            onActionRequestClick(request.getRequest().getId(), action.getId());
                            return false;
                        }
                    });
        }
        popupMenu.show();
    }

    @Override
    public void onActionRequestClick(final int requestId, final int actionId) {
        switch (actionId) {
            case CANCEL:
                new LovelyTextInputDialog(mContext).setTopColorRes(R.color.colorPrimary)
                        .setTitle(R.string.msg_cancel_request)
                        .setIcon(R.drawable.ic_error_white)
                        .setConfirmButton(android.R.string.ok,
                                new LovelyTextInputDialog.OnTextInputConfirmListener() {
                                    @Override
                                    public void onTextInputConfirmed(String text) {
                                        if (TextUtils.isEmpty(text)) {
                                            return;
                                        }
                                        mPresenter.cancelRequest(requestId, actionId, text);
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel, null)
                        .setInputFilter(R.string.error_empty_description,
                                new LovelyTextInputDialog.TextFilter() {
                                    @Override
                                    public boolean check(String s) {
                                        return !TextUtils.isEmpty(s);
                                    }
                                })
                        .show();
                break;
            default:
                mPresenter.updateActionRequest(requestId, actionId);
                break;
        }
    }

    @Override
    public void showMessage(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void onDetailRequestClick(Request request) {
        mFragment.startActivityForResult(
                RequestDetailActivity.getInstance(mContext, request.getId()),
                REQUEST_DETAIL);
    }

    @Override
    public void onAddDeviceClick(int requestId) {
    }

    @Bindable
    public boolean isRefresh() {
        return mIsRefresh;
    }

    public void setRefresh(boolean refresh) {
        mIsRefresh = refresh;
        notifyPropertyChanged(BR.refresh);
    }

    public SwipeRefreshLayout.OnRefreshListener getRefreshLayout() {
        return mRefreshLayout;
    }

    @Override
    public void onDeviceDashBoardClick(Device device) {
        ((OnDashBoardDetailClickListener) mFragment.getActivity()).onItemClick(device);
    }
}
