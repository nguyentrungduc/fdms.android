package com.framgia.fdms.screen.device.mydevice;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import com.android.databinding.library.baseAdapters.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.screen.device.mydevice.mydevicedetail.MyDeviceDetailFragment;
import com.framgia.fdms.screen.device.mydevice.mydevicedetail.MyDeviceType;

/**
 * Exposes the data to be used in the MyDevice screen.
 */

public class MyDeviceViewModel extends BaseObservable implements MyDeviceContract.ViewModel {

    private MyDeviceContract.Presenter mPresenter;
    private MyDevicePagerAdapter mAdapter;

    public MyDeviceViewModel(Fragment fragment) {
        mAdapter = new MyDevicePagerAdapter(fragment.getChildFragmentManager());
        mAdapter.addFragment(MyDeviceDetailFragment.newInstance(MyDeviceType.ALL),
            fragment.getString(R.string.title_device_all));
        mAdapter.addFragment(MyDeviceDetailFragment.newInstance(MyDeviceType.USING),
            fragment.getString(R.string.title_device_using));
        mAdapter.addFragment(MyDeviceDetailFragment.newInstance(MyDeviceType.RETURNED),
            fragment.getString(R.string.title_device_returned));
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
}
