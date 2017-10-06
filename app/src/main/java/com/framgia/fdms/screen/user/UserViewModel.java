package com.framgia.fdms.screen.user;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.screen.device.mydevice.MyDevicePagerAdapter;
import com.framgia.fdms.screen.device.mydevice.mydevicedetail.MyDeviceDetailFragment;
import com.framgia.fdms.screen.device.mydevice.mydevicedetail.MyDeviceType;

/**
 * Exposes the data to be used in the User screen.
 */

public class UserViewModel extends BaseObservable implements UserContract.ViewModel {

    private UserContract.Presenter mPresenter;
    private UserActivity mActivity;
    private MyDevicePagerAdapter mAdapter;
    private int mCurrentTab;
    private String mTitle;

    public UserViewModel(UserActivity activity, User user) {
        mActivity = activity;
        mAdapter = new MyDevicePagerAdapter(activity.getSupportFragmentManager());
        mAdapter.addFragment(MyDeviceDetailFragment.newInstance(user, MyDeviceType.ALL),
            activity.getString(R.string.title_device_all));
        mAdapter.addFragment(MyDeviceDetailFragment.newInstance(user, MyDeviceType.USING),
            activity.getString(R.string.title_device_using));
        mAdapter.addFragment(MyDeviceDetailFragment.newInstance(user, MyDeviceType.RETURNED),
            activity.getString(R.string.title_device_returned));
        setTitle(user.getName());
        setCurrentTab(MyDeviceType.USING);
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
    public void setPresenter(UserContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Bindable
    public UserActivity getActivity() {
        return mActivity;
    }

    public void setActivity(UserActivity activity) {
        mActivity = activity;
        notifyPropertyChanged(BR.activity);
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

    @Bindable
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
        notifyPropertyChanged(BR.title);
    }
}
