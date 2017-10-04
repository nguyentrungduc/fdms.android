package com.framgia.fdms.screen.requestdetail.listdeviceassignment;

/**
 * Listens to user actions from the UI ({@link ListDeviceAssignmentFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
final class ListDeviceAssignmentPresenter implements ListDeviceAssignmentContract.Presenter {

    private final ListDeviceAssignmentContract.ViewModel mViewModel;

    ListDeviceAssignmentPresenter(ListDeviceAssignmentContract.ViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
