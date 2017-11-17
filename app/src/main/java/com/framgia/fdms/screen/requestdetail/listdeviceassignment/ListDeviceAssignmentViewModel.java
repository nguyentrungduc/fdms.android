package com.framgia.fdms.screen.requestdetail.listdeviceassignment;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;

import com.framgia.fdms.BR;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Request;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import com.framgia.fdms.data.model.Request.DeviceRequest;
import com.framgia.fdms.screen.devicedetail.DeviceDetailActivity;
import com.framgia.fdms.utils.navigator.Navigator;

/**
 * Exposes the data to be used in the Devicedetail screen.
 */

public class ListDeviceAssignmentViewModel extends BaseObservable
        implements ListDeviceAssignmentContract.ViewModel,
        BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<DeviceRequest> {

    private ListDeviceAssignmentContract.Presenter mPresenter;
    private ListDeviceAssignmentAdapter mAdapter;
    private int mEmptyViewVisible = GONE;
    private Navigator mNavigator;

    public ListDeviceAssignmentViewModel(Fragment fragment, Request request) {
        mAdapter = new ListDeviceAssignmentAdapter(fragment.getContext(), this);
        mAdapter.addItem(request.getDevices());
        mAdapter.setListenner(this);
        setEmptyViewVisible(
                request.getDevices() != null && request.getDevices().size() != 0 ? GONE : VISIBLE);
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
    public void setPresenter(ListDeviceAssignmentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public ListDeviceAssignmentAdapter getAdapter() {
        return mAdapter;
    }

    @Bindable
    public int getEmptyViewVisible() {
        return mEmptyViewVisible;
    }

    public void setEmptyViewVisible(int emptyViewVisible) {
        mEmptyViewVisible = emptyViewVisible;
        notifyPropertyChanged(BR.emptyStateVisibility);
    }

    @Override
    public void onItemRecyclerViewClick(DeviceRequest item) {
        mPresenter.getDeviceByDeviceId(item.getId());
    }

    @Override
    public void onGetDeviceDetailSuccess(Device device) {
        mNavigator.startActivity(DeviceDetailActivity.getInstance(mNavigator.getContext(), device));
    }

    @Override
    public void onGetDeviceDetailFailure(String message) {
        mNavigator.showToast(message);
    }
}
