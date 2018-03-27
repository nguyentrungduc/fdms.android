package com.framgia.fdms.screen.inventorydevice;

import com.framgia.fdms.data.model.Status;
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
    private CompositeDisposable mDisposable;


    public InventoryDevicePresenter(DeviceRepository deviceRepository) {
        mDeviceRepository = deviceRepository;
    }


    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }


    @Override
    public void getListDevice(Status status) {

    }

    @Override
    public void postInventory() {

    }
}
