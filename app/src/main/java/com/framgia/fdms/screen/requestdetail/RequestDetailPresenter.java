package com.framgia.fdms.screen.requestdetail;

/**
 * Listens to user actions from the UI ({@link RequestDetailActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
final class RequestDetailPresenter implements RequestDetailContract.Presenter {

    private final RequestDetailContract.ViewModel mViewModel;

    RequestDetailPresenter(RequestDetailContract.ViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
