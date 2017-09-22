package com.framgia.fdms.screen.device.mydevice.mydevicedetail;

/**
 * Listens to user actions from the UI ({@link MyDeviceDetailFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
final class MyDeviceDetailPresenter implements MyDeviceDetailContract.Presenter {
    private static final String TAG = MyDeviceDetailPresenter.class.getName();

    private final MyDeviceDetailContract.ViewModel mViewModel;

    public MyDeviceDetailPresenter(MyDeviceDetailContract.ViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }
}
