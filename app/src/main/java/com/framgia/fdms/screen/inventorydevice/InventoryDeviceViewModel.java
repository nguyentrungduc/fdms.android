package com.framgia.fdms.screen.inventorydevice;

import android.app.Activity;
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
        InventoryDeviceContract.ViewModel{

    private Device mDevice;
    private User mUser;
    InventoryDevicePresenter mInventoryDevicePresenter;

    public InventoryDeviceViewModel() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void setPresenter(InventoryDeviceContract.Presenter presenter) {

    }


}
