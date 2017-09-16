package com.framgia.fdms.screen.deviceusingmanager;

/**
 * Listens to user actions from the UI ({@link DeviceUsingManagerFragment}),
 * retrieves the data and updates
 * the UI as required.
 */
final class DeviceUsingManagerPresenter implements DeviceUsingManagerContract.Presenter {

    private final DeviceUsingManagerContract.ViewModel mViewModel;

    public DeviceUsingManagerPresenter(DeviceUsingManagerContract.ViewModel viewModel) {
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
        //// TODO: 9/16/2017  
    }
}
