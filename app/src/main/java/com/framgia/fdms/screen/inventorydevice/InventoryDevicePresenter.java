package com.framgia.fdms.screen.inventorydevice;

import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.DeviceReturnRepository;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.screen.returndevice.ReturnDeviceContract;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Sony on 3/27/2018.
 */

public class InventoryDevicePresenter implements InventoryDeviceContract.Presenter {

    private UserRepository mUserRepository;
    private DeviceRepository mDeviceRepository;
    private InventoryDeviceViewModel mViewModel;


    public InventoryDevicePresenter(InventoryDeviceContract.ViewModel viewModel,
                        DeviceRepository deviceRepository) {
        mDeviceRepository = deviceRepository;
        mViewModel = (InventoryDeviceViewModel) viewModel;

    }


    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }


    @Override
    public void getListDevice(User user) {

    }

    @Override
    public void postInventory() {

    }
}
