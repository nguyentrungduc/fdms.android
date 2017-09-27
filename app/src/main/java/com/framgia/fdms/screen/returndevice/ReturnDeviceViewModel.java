package com.framgia.fdms.screen.returndevice;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.screen.devicedetail.DeviceDetailActivity;
import com.framgia.fdms.screen.scanner.ScannerActivity;
import com.framgia.fdms.screen.selection.SelectionActivity;
import com.framgia.fdms.screen.selection.SelectionType;
import com.framgia.fdms.utils.navigator.Navigator;
import com.framgia.fdms.utils.permission.PermissionUtil;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static com.framgia.fdms.screen.selection.SelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_CONTENT;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_SCANNER;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_USER_BORROW;
import static com.framgia.fdms.utils.permission.PermissionUtil.MY_PERMISSIONS_REQUEST_CAMERA;

import com.framgia.fdms.BR;

/**
 * Exposes the data to be used in the ReturnDevice screen.
 */

public class ReturnDeviceViewModel extends BaseObservable
    implements ReturnDeviceContract.ViewModel {

    private ReturnDeviceActivity mActivity;
    private ReturnDeviceContract.Presenter mPresenter;
    private DeviceReturnAdapter mAdapter;
    private boolean mProgressBarVisibility;

    private Status mStaff;
    private List<Integer> mListDeviceId;
    private String mContextQrCode;
    private Navigator mNavigator;
    private int mEmptyViewVisible;

    public ReturnDeviceViewModel(ReturnDeviceActivity activity) {
        mActivity = activity;
        mAdapter = new DeviceReturnAdapter(this, new ArrayList<Device>());
        mListDeviceId = new ArrayList<>();
        mNavigator = new Navigator(activity);
        mEmptyViewVisible = GONE;
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
    public void setPresenter(ReturnDeviceContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || data.getExtras() == null || resultCode != Activity.RESULT_OK) {
            return;
        }
        Bundle bundle = data.getExtras();
        switch (requestCode) {
            case REQUEST_USER_BORROW:
                mContextQrCode = "";
                Status status = bundle.getParcelable(BUNDLE_DATA);
                if (status == null || status.getId() == OUT_OF_INDEX) {
                    return;
                }
                setStaff(status);
                mPresenter.getDevicesOfBorrower(status.getId());
                break;
            case REQUEST_SCANNER:
                mContextQrCode = bundle.getString(BUNDLE_CONTENT);
                if (mAdapter.updateScanDevice(mContextQrCode)) {
                    mNavigator.showToast(mActivity.getString(R.string.msg_scan_sucess));
                    return;
                }
                mPresenter.getDeviceByCode(mContextQrCode);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemDeviceClick(Device device) {
        mNavigator.startActivity(DeviceDetailActivity.getInstance(mNavigator.getContext(), device));
    }

    @Override
    public void onCheckedChanged(boolean checked, Device device, int position) {
        device.setSelected(checked);
    }

    @Override
    public void onSelectedUserReturn() {
        mActivity.startActivityForResult(
            SelectionActivity.getInstance(mActivity.getApplicationContext(),
                SelectionType.USER_BORROW), REQUEST_USER_BORROW);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
        int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA
            && grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScannerActivity();
        } else {
            Snackbar.make(mActivity.findViewById(android.R.id.content),
                R.string.msg_denied_read_camera, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onReturnDeviceEmpty() {
        mNavigator.showToast(R.string.msg_device_empty);
    }

    @Override
    public void onStartScannerDevice() {
        if (PermissionUtil.checkCameraPermission(mActivity)) {
            startScannerActivity();
        }
    }

    private void startScannerActivity() {
        mActivity.startActivityForResult(
            ScannerActivity.newIntent(mActivity.getApplicationContext()), REQUEST_SCANNER);
    }

    @Override
    public void onGetDeviceSuccess(Device device) {
        if (mStaff == null) {
            setStaff(
                new Status(Integer.parseInt(device.getUser().getId()), device.getUser().getName()));
            mPresenter.getDevicesOfBorrower(Integer.parseInt(device.getUser().getId()));
            return;
        }
        if (device.getUser() == null) {
            mNavigator.showToast(
                mActivity.getString(R.string.msg_not_device_in_device_brorows_available,
                    mStaff.getName()));
            return;
        }
        if (String.valueOf(mStaff.getId()).equals(device.getUser().getId())) {
            mAdapter.updateScanDevice(device.getDeviceCode());
            return;
        }
        mNavigator.showToast(
            mActivity.getString(R.string.msg_not_device_in_device_brorows, mStaff.getName(),
                device.getUser().getName()));
    }

    @Override
    public void onReturnDeviceSuccess(String message) {
        mNavigator.showToast(message);
        mPresenter.getDevicesOfBorrower(mStaff.getId());
    }

    @Override
    public void showProgressbar() {
        setProgressBarVisibility(true);
    }

    @Override
    public void hideProgressbar() {
        setProgressBarVisibility(false);
    }

    @Override
    public void onLoadError(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    private void getAllDeviceBorrowOfUser(int userId) {
        mPresenter.getDevicesOfBorrower(userId);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeviceLoaded(List<Device> devices) {
        if (mAdapter.updateWithDeviceCode(devices, mContextQrCode)) {
            mNavigator.showToast(mActivity.getString(R.string.msg_scan_sucess));
        }
    }

    @Override
    public void onReturnDeviceClick() {
        mListDeviceId.clear();
        for (Device item : mAdapter.getDevices()) {
            if (item.isSelected()) {
                mListDeviceId.add(item.getId());
            }
        }
        mPresenter.returnDevice(mListDeviceId);
    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }

    public DeviceReturnAdapter getAdapter() {
        return mAdapter;
    }

    @Bindable
    public Status getStaff() {
        return mStaff;
    }

    public void setStaff(Status staff) {
        mStaff = staff;
        notifyPropertyChanged(BR.staff);
    }

    @Bindable
    public boolean isProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public void setProgressBarVisibility(boolean progressBarVisibility) {
        mProgressBarVisibility = progressBarVisibility;
        notifyPropertyChanged(BR.progressBarVisibility);
    }

    @Bindable
    public int getEmptyViewVisible() {
        return mEmptyViewVisible;
    }

    public void setEmptyViewVisible(int emptyViewVisible) {
        mEmptyViewVisible = emptyViewVisible;
        notifyPropertyChanged(BR.emptyViewVisible);
    }
}
