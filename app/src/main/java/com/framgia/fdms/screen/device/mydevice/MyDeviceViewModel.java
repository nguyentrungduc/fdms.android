package com.framgia.fdms.screen.device.mydevice;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import com.android.databinding.library.baseAdapters.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.screen.device.mydevice.mydevicedetail.MyDeviceDetailFragment;
import com.framgia.fdms.screen.device.mydevice.mydevicedetail.MyDeviceType;

/**
 * Exposes the data to be used in the MyDevice screen.
 */

public class MyDeviceViewModel extends BaseObservable implements MyDeviceContract.ViewModel {

    private MyDeviceContract.Presenter mPresenter;
    private MyDevicePagerAdapter mAdapter;
    private int mCurrentTab;
    private Fragment mFragment;

    public MyDeviceViewModel(Fragment fragment) {
        mFragment = fragment;
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
    public void setPresenter(MyDeviceContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Bindable
    public MyDevicePagerAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(MyDevicePagerAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Bindable
    public int getCurrentTab() {
        return mCurrentTab;
    }

    public void setCurrentTab(int currentTab) {
        mCurrentTab = currentTab;
        notifyPropertyChanged(BR.currentTab);
    }

    @Override
    public void onGetUserSuccess(User user) {
        mAdapter = new MyDevicePagerAdapter(mFragment.getChildFragmentManager());
        mAdapter.addFragment(MyDeviceDetailFragment.newInstance(user, MyDeviceType.ALL),
            mFragment.getString(R.string.title_device_all));
        mAdapter.addFragment(MyDeviceDetailFragment.newInstance(user, MyDeviceType.USING),
            mFragment.getString(R.string.title_device_using));
        mAdapter.addFragment(MyDeviceDetailFragment.newInstance(user, MyDeviceType.RETURNED),
            mFragment.getString(R.string.title_device_returned));
        notifyPropertyChanged(BR.adapter);
        setCurrentTab(MyDeviceType.USING);
    }

    @Override
    public void onGetUserFailure(String msg) {

    }
}
