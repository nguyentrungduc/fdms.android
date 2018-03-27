package com.framgia.fdms.screen.inventorydevice;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.framgia.fdms.R;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.screen.returndevice.ReturnDeviceViewModel;

public class InventoryDeviceActivity extends AppCompatActivity {

    InventoryDeviceContract.Presenter mPresenter;
    InventoryDeviceContract.ViewModel mViewModel;
    private ActivityInvetoryDevice mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new InventoryDevicePresenter(new UserRepository(User))
        mViewModel.setPresenter(mPresenter);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_inventory_device);
        mBinding.setViewModel((ReturnDeviceViewModel) mViewModel);
    }

}
