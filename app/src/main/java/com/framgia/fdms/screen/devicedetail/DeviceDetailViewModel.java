package com.framgia.fdms.screen.devicedetail;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.android.databinding.library.baseAdapters.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.screen.devicedetail.history.DeviceDetailHistoryFragment;
import com.framgia.fdms.screen.devicedetail.infomation.DeviceInfomationFragment;
import com.framgia.fdms.screen.devicedetail.usinghistory.DeviceUsingHistoryFragment;
import com.framgia.fdms.utils.navigator.Navigator;
import com.github.clans.fab.FloatingActionMenu;
import java.util.ArrayList;
import java.util.List;

import static com.framgia.fdms.screen.devicedetail.DeviceDetailPagerAdapter.DeviceDetailPage
    .DEVICE_INFOMATION;
import static com.framgia.fdms.utils.Constant.USING;

/**
 * Exposes the data to be used in the Devicedetail screen.
 */

public class DeviceDetailViewModel extends BaseObservable
    implements DeviceDetailContract.ViewModel {

    private DeviceDetailContract.Presenter mPresenter;
    private DeviceDetailPagerAdapter mAdapter;
    private Context mContext;
    private AppCompatActivity mActivity;
    private DeviceInfomationFragment mInfomationFragment;
    private ObservableInt mFloatingVisible = new ObservableInt(View.VISIBLE);
    private Device mDevice;
    private ObservableField<Integer> mProgressBarVisibility = new ObservableField<>();
    private boolean mIsBoManager;
    private boolean mIsUsingDevice;
    private Navigator mNavigator;

    public DeviceDetailViewModel(AppCompatActivity activity, Device device, Navigator navigator) {
        mActivity = activity;
        mNavigator = navigator;
        mContext = mActivity.getApplicationContext();
        mDevice = device;
        mInfomationFragment = DeviceInfomationFragment.newInstance(mDevice);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(mInfomationFragment);
        fragments.add(DeviceDetailHistoryFragment.newInstance(mDevice.getId()));
        fragments.add(DeviceUsingHistoryFragment.newInstance(mDevice.getDeviceCode()));
        mAdapter =
            new DeviceDetailPagerAdapter(mContext, mActivity.getSupportFragmentManager(), fragments,
                mDevice.getId());
        if (mDevice.getDeviceStatusId() == USING) {
            setUsingDevice(true);
            return;
        }
        setUsingDevice(false);
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
    public void setPresenter(DeviceDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onEditDevice(FloatingActionMenu floatingActionsMenu) {
        floatingActionsMenu.close(true);
        if (mInfomationFragment != null) mInfomationFragment.onStartEditDevice();
    }

    @Override
    public void onGetDeviceSuccess(Device device) {
        setDevice(device);
    }

    @Override
    public void onGetDeviceError(String error) {
        mNavigator.showToast(error);
    }

    @Override
    public void onGetUserSuccess(User user) {
        if (user.getRole() == null) {
            return;
        }
        setBoManager(user.isBo());
    }

    @Override
    public void onGetUserError(String error) {
        mNavigator.showToast(error);
    }

    public ObservableField<Integer> getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    @Bindable
    public Device getDevice() {
        return mDevice;
    }

    public void setDevice(Device device) {
        mDevice = device;
        notifyPropertyChanged(BR.device);
    }

    public void updateFloatingVisible(int position) {
        if (position == DEVICE_INFOMATION && isBoManager()) {
            mFloatingVisible.set(View.VISIBLE);
        } else {
            mFloatingVisible.set(View.GONE);
        }
    }

    public void onDeleteDeviceClick(FloatingActionMenu floatingActionsMenu) {
        floatingActionsMenu.close(true);
        new AlertDialog.Builder(mActivity).setTitle(mActivity.getString(R.string.title_delete))
            .setMessage(mActivity.getString(R.string.msg_delete_producer)
                + " "
                + mDevice.getProductionName())
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mInfomationFragment != null) mInfomationFragment.onDeleteDevice();
                }
            })
            .setNegativeButton(android.R.string.no, null)
            .create()
            .show();
    }

    public DeviceDetailPagerAdapter getAdapter() {
        return mAdapter;
    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }

    public ObservableInt getFloatingVisible() {
        return mFloatingVisible;
    }

    @Bindable
    public boolean isUsingDevice() {
        return mIsUsingDevice;
    }

    private void setUsingDevice(boolean usingDevice) {
        mIsUsingDevice = usingDevice;
        notifyPropertyChanged(BR.usingDevice);
    }

    @Bindable
    private boolean isBoManager() {
        return mIsBoManager;
    }

    private void setBoManager(boolean boManager) {
        mIsBoManager = boManager;
        notifyPropertyChanged(BR.boManager);
    }
}
