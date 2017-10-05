package com.framgia.fdms.screen.requestdetail.listdeviceassignment;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import com.framgia.fdms.BR;
import com.framgia.fdms.data.model.Request;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Exposes the data to be used in the Devicedetail screen.
 */

public class ListDeviceAssignmentViewModel extends BaseObservable
    implements ListDeviceAssignmentContract.ViewModel {

    private ListDeviceAssignmentContract.Presenter mPresenter;
    private ListDeviceAssignmentAdapter mAdapter;
    private int mEmptyViewVisible = GONE;

    public ListDeviceAssignmentViewModel(Fragment fragment, Request request) {
        mAdapter = new ListDeviceAssignmentAdapter(fragment.getContext(), this);
        mAdapter.onUpdatePage(request.getDevices());
        setEmptyViewVisible(
            request.getDevice() != null && request.getDevice().size() != 0 ? GONE : VISIBLE);
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
}
