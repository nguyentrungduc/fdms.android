package com.framgia.fdms.screen.inventorydevice;

import com.framgia.fdms.data.model.DeviceInventory;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.DeviceReturnRepository;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Sony on 3/27/2018.
 */

public class InventoryDevicePresenter implements InventoryDeviceContract.Presenter {

    private DeviceReturnRepository mDeviceReturnRepository;
    private InventoryDeviceViewModel mViewModel;
    private CompositeDisposable mDisposable;

    public InventoryDevicePresenter(DeviceReturnRepository deviceReturnRepository) {
        mDeviceReturnRepository = deviceReturnRepository;
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
    public void postInventory(DeviceInventory deviceInventory) {

    }
}
