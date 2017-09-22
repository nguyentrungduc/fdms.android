package com.framgia.fdms.screen.device.mydevice;

/**
 * Listens to user actions from the UI ({@link MyDeviceFragment}), retrieves the data and updates
 * the UI as required.
 */
final class MyDevicePresenter implements MyDeviceContract.Presenter {
    private static final String TAG = MyDevicePresenter.class.getName();

    private final MyDeviceContract.ViewModel mViewModel;

    public MyDevicePresenter(MyDeviceContract.ViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }
}
