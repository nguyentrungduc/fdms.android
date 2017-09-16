package com.framgia.fdms.screen.deviceusingmanager.base;

import com.framgia.fdms.screen.deviceusingmanager.DeviceUsingManagerContract;

/**
 * Created by lamvu on 06/09/2017.
 */

public class DeviceUsingBasePresenter implements DeviceUsingManagerContract.Presenter {

    private final DeviceUsingManagerContract.ViewModel mViewModel;

    public DeviceUsingBasePresenter(DeviceUsingManagerContract.ViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void getDeviceUsingHistory() {

    }

    @Override
    public void loadMoreData() {

    }
}
