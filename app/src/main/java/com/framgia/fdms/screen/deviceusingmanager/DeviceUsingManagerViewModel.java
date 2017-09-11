package com.framgia.fdms.screen.deviceusingmanager;

import android.databinding.BaseObservable;
import android.support.v4.app.FragmentActivity;

import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.screen.device.ViewPagerAdapter;
import com.framgia.fdms.screen.deviceusingmanager.base.DeviceUsingBaseFragment;

import java.util.List;

/**
 * Exposes the data to be used in the DeviceUsing screen.
 */

public class DeviceUsingManagerViewModel extends BaseObservable
        implements DeviceUsingManagerContract.ViewModel {

    private DeviceUsingManagerContract.Presenter mPresenter;

    private ViewPagerAdapter mAdapter;

    public ViewPagerAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(ViewPagerAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    public DeviceUsingManagerViewModel(FragmentActivity activity) {
        mAdapter = new ViewPagerAdapter(activity.getSupportFragmentManager());
        setAdapter(mAdapter);

        mAdapter.addFragment(DeviceUsingBaseFragment.newInstance(
                activity.getString(R.string.fragment_all_device_using)),
                activity.getString(R.string.fragment_all_device_using));
        mAdapter.addFragment(DeviceUsingBaseFragment.newInstance(
                activity.getString(R.string.fragment_using_device_using)),
                activity.getString(R.string.fragment_using_device_using));
        mAdapter.addFragment(DeviceUsingBaseFragment.newInstance(
                activity.getString(R.string.fragment_returned_device_using)),
                activity.getString(R.string.fragment_returned_device_using));
        notifyPropertyChanged(BR.adapter);
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
    public void setPresenter(DeviceUsingManagerContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void onGetDeviceUsingHistorySuccess(List<DeviceUsingHistory> deviceUsingHistories) {
        //Do this on DeviceUsingBaseViewModel
    }

    @Override
    public void onGetDeviceUsingHistoryFailed() {
        //Do this on DeviceUsingBaseViewModel
    }
}
