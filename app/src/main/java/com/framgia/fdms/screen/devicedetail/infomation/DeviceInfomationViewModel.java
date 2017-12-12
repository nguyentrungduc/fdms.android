package com.framgia.fdms.screen.devicedetail.infomation;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;

import com.android.databinding.library.baseAdapters.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.screen.devicecreation.CreateDeviceActivity;
import com.framgia.fdms.screen.devicecreation.DeviceStatusType;
import com.framgia.fdms.screen.devicedetail.DeviceInfoListenner;
import com.framgia.fdms.screen.meetingroomdetail.DetailMeetingRoomActivity;
import com.framgia.fdms.screen.user.UserActivity;
import com.framgia.fdms.utils.navigator.Navigator;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_DEVICE;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_SUCCESS;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_EDIT;

/**
 * Exposes the data to be used in the Deviceinfomation screen.
 */

public class DeviceInfomationViewModel extends BaseObservable
        implements DeviceInfomationContract.ViewModel, DeviceInfoListenner {

    private DeviceInfomationContract.Presenter mPresenter;
    private Device mDevice;
    private Context mContext;
    private ObservableField<Integer> mProgressBarVisibility = new ObservableField<>();
    private FragmentActivity mActivity;
    private Navigator mNavigator;

    public DeviceInfomationViewModel(Context context, FragmentActivity activity, Device device,
                                     Navigator navigator) {
        mContext = context;
        mActivity = activity;
        mDevice = device;
        mNavigator = navigator;
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
    public void setPresenter(DeviceInfomationContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetDeviceSuccess(Device device) {
        setDevice(device);
    }

    @Override
    public void onEditDevice() {
        mActivity.startActivityForResult(
                CreateDeviceActivity.getInstance(mContext, mDevice, DeviceStatusType.EDIT),
                REQUEST_EDIT);
    }

    @Override
    public void onDeleteDevice() {
        mPresenter.deleteDevice(mDevice);
    }

    @Override
    public void onDeleteDeviceSuccess(Device device, String message) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_SUCCESS, message);
        bundle.putParcelable(BUNDLE_DEVICE, device);
        intent.putExtras(bundle);
        mNavigator.finishActivityWithResult(intent, RESULT_OK);
    }

    @Override
    public void onDeleteDeviceError(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || data == null) return;
        Device device = data.getExtras().getParcelable(BUNDLE_DEVICE);
        if (requestCode == REQUEST_EDIT) {
            setDevice(device);
        }
    }

    @Override
    public void onLoadData() {
        showInformation();
    }

    @Override
    public void onError() {
        Snackbar.make(mActivity.findViewById(android.R.id.content), R.string.error_device_detail,
                Snackbar.LENGTH_SHORT).show();
    }

    public ObservableField<Integer> getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    @Bindable
    public Device getDevice() {
        return mDevice;
    }

    public void setDevice(Device device) {
        mDevice.cloneDevice(device);
        notifyPropertyChanged(BR.device);
    }

    private void showInformation() {
        if (mDevice == null) onError();
        onGetDeviceSuccess(mDevice);
    }

    @Override
    public void onCurrentUsingPersonClicked(Device device) {
        if (device.getUser() != null) {
            mNavigator.startActivity(UserActivity.getInstance(mNavigator.getContext(),
                    device.getUser()));
        } else if (device.getMeetingRoom() != null) {
            mNavigator.startActivity(DetailMeetingRoomActivity.getInstance(mNavigator.getContext(),
                    device.getMeetingRoom()));
        }

    }
}
