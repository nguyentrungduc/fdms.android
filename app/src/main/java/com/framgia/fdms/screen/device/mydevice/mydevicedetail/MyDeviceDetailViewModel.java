package com.framgia.fdms.screen.device.mydevice.mydevicedetail;

/**
 * Exposes the data to be used in the MyDeviceDetail screen.
 */

public class MyDeviceDetailViewModel implements MyDeviceDetailContract.ViewModel {

    private MyDeviceDetailContract.Presenter mPresenter;

    public MyDeviceDetailViewModel() {
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
    public void setPresenter(MyDeviceDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
