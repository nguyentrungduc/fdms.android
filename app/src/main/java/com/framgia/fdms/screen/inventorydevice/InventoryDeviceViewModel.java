package com.framgia.fdms.screen.inventorydevice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.framgia.fdms.BR;
import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.screen.devicedetail.DeviceDetailActivity;
import com.framgia.fdms.screen.returndevice.DeviceReturnAdapter;
import com.framgia.fdms.screen.returndevice.ReturnDeviceActivity;
import com.framgia.fdms.screen.returndevice.ReturnDeviceContract;
import com.framgia.fdms.screen.scanner.ScannerActivity;
import com.framgia.fdms.screen.selection.SelectionActivity;
import com.framgia.fdms.screen.selection.SelectionType;
import com.framgia.fdms.utils.navigator.Navigator;
import com.framgia.fdms.utils.permission.PermissionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

import static android.view.View.GONE;
import static com.framgia.fdms.screen.selection.SelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_CONTENT;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_SCANNER;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_USER_BORROW;
import static com.framgia.fdms.utils.permission.PermissionUtil.MY_PERMISSIONS_REQUEST_CAMERA;

/**
 * Created by Sony on 3/27/2018.
 */
public class InventoryDeviceViewModel extends BaseObservable implements
        InventoryDeviceContract.ViewModel, OnDeviceListenner {

    private Device mDevice;
    private Status mStaff;
    private InventoryDeviceActivity mActivity;
    private InventoryDeviceAdapter mAdapter;
    private InventoryDeviceContract.Presenter mPresenter;
    private String mContextQrCode;

    public InventoryDeviceViewModel(Activity activity) {
        mActivity = (InventoryDeviceActivity) activity;
        mAdapter = new InventoryDeviceAdapter(new ArrayList<Device>(), activity.getBaseContext());
        mAdapter.setDeviceClickListener(this);
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
    public void setPresenter(InventoryDeviceContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Bindable
    public Device getDevice() {
        return mDevice;
    }

    public void setDevice(Device device) {
        mDevice = device;
        notifyPropertyChanged(BR.device);
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
    public String getContextQrCode() {
        return mContextQrCode;
    }

    public void setContextQrCode(String contextQrCode) {
        mContextQrCode = contextQrCode;
        notifyPropertyChanged(BR.contextQrCode);
    }

    @Bindable
    public InventoryDeviceAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(InventoryDeviceAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Override
    public void onSelectedUser() {
        mActivity.startActivityForResult(
                SelectionActivity.getInstance(mActivity.getApplicationContext(),
                        SelectionType.USER_BORROW), REQUEST_USER_BORROW);
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
                break;
            default:
                break;
        }
    }

    @Override
    public void onDeviceClick(Device device, int position) {

    }

    @Override
    public void onDeviceCommentClick(Device device, int position) {

    }
}