package com.framgia.fdms.screen.requestdetail.listdeviceassignment;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.support.v4.app.Fragment;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.screen.requestdetail.information.RequestInformationFragment;
import com.framgia.fdms.utils.navigator.Navigator;

/**
 * Exposes the data to be used in the Devicedetail screen.
 */

public class ListDeviceAssignmentViewModel extends BaseObservable
    implements ListDeviceAssignmentContract.ViewModel {

    private ListDeviceAssignmentContract.Presenter mPresenter;
    private Context mContext;
    private Fragment mFragment;
    private Request.DeviceRequest mDeviceRequest;
    private ListDeviceAssignmentAdapter mAdapter;
    private ObservableField<Integer> mProgressBarVisibility = new ObservableField<>();

    public ListDeviceAssignmentViewModel(Fragment fragment, Request request) {
        mFragment = fragment;
        mContext = mFragment.getContext();
        mAdapter = new ListDeviceAssignmentAdapter(mContext, this);
        mAdapter.onUpdatePage(request.getDevices());
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

    public ObservableField<Integer> getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public ListDeviceAssignmentAdapter getAdapter() {
        return mAdapter;
    }
}
