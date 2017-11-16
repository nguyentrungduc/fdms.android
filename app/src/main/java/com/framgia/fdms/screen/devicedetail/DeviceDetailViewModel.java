package com.framgia.fdms.screen.devicedetail;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.framgia.fdms.BR;
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

import static com.framgia.fdms.data.anotation.Permission.BO_MANAGER;
import static com.framgia.fdms.data.anotation.Permission.BO_STAFF;
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
    private boolean mIsAllowEditDeleteDevice = true;
    private Device mDevice;
    private ObservableField<Integer> mProgressBarVisibility = new ObservableField<>();

    private boolean mIsUsingDevice;
    private Navigator mNavigator;
    private User mUser;

    public DeviceDetailViewModel(AppCompatActivity activity, Device device, Navigator navigator) {
        mActivity = activity;
        mNavigator = navigator;
        mContext = mActivity.getApplicationContext();
        mDevice = device;
        List<Fragment> fragments = new ArrayList<>();
        mInfomationFragment = DeviceInfomationFragment.newInstance(mDevice);
        fragments.add(mInfomationFragment);
        fragments.add(DeviceDetailHistoryFragment.newInstance(mDevice.getId()));
        fragments.add(DeviceUsingHistoryFragment.newInstance(mDevice.getDeviceCode()));
        mAdapter =
                new DeviceDetailPagerAdapter(mContext,
                        mActivity.getSupportFragmentManager(),
                        fragments,
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
        if (user == null) {
            return;
        }
        mUser = user;
        setIsAllowEditDeleteDevice(isAllowEditDeleteDevice(DEVICE_INFOMATION, mUser.getRole()));
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
        setIsAllowEditDeleteDevice(isAllowEditDeleteDevice(position, mUser.getRole()));
    }

    public boolean isAllowEditDeleteDevice(int position, int permission) {
        return position == DEVICE_INFOMATION &&
                (permission == BO_MANAGER || permission == BO_STAFF);
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

    @Bindable
    public boolean getIsAllowEditDeleteDevice() {
        return mIsAllowEditDeleteDevice;
    }

    public void setIsAllowEditDeleteDevice(boolean isAllowEditDeleteDevice) {
        mIsAllowEditDeleteDevice = isAllowEditDeleteDevice;
        notifyPropertyChanged(BR.isAllowEditDeleteDevice);
    }

    @Bindable
    public boolean isUsingDevice() {
        return mIsUsingDevice;
    }

    private void setUsingDevice(boolean usingDevice) {
        mIsUsingDevice = usingDevice;
        notifyPropertyChanged(BR.usingDevice);
    }
}
