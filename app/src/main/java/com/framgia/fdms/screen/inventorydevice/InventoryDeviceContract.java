package com.framgia.fdms.screen.inventorydevice;

import android.content.Intent;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.User;

import java.util.List;

/**
 * Created by Sony on 3/27/2018.
 */

public class InventoryDeviceContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {

    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {

        void getListDevice(User user);

        void postInventory();

    }
}
