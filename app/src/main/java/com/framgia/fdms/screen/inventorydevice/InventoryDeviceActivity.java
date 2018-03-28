package com.framgia.fdms.screen.inventorydevice;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.framgia.fdms.R;
import com.framgia.fdms.data.source.DeviceReturnRepository;
import com.framgia.fdms.databinding.ActivityInventoryDeviceBinding;

public class InventoryDeviceActivity extends AppCompatActivity {

    InventoryDeviceContract.ViewModel mViewModel;

    public static Intent getInstance(Context context) {
        return new Intent(context, InventoryDeviceActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new InventoryDeviceViewModel(this);
        DeviceReturnRepository deviceReturnRepository = new DeviceReturnRepository();
        InventoryDeviceContract.Presenter mPresenter = new InventoryDevicePresenter(
                deviceReturnRepository);
        mViewModel.setPresenter(mPresenter);

        ActivityInventoryDeviceBinding mBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_inventory_device);
        mBinding.setViewModel((InventoryDeviceViewModel) mViewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onStop() {
        mViewModel.onStop();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mViewModel != null) mViewModel.onActivityResult(requestCode, resultCode, data);
    }

}
