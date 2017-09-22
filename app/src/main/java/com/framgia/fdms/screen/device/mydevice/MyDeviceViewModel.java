package com.framgia.fdms.screen.device.mydevice;

/**
 * Exposes the data to be used in the MyDevice screen.
 */

public class MyDeviceViewModel implements MyDeviceContract.ViewModel {

    private MyDeviceContract.Presenter mPresenter;

    public MyDeviceViewModel() {
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
    public void setPresenter(MyDeviceContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
