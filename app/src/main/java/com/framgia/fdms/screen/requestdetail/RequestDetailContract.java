package com.framgia.fdms.screen.requestdetail;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Request;

/**
 * This specifies the contract between the view and the presenter.
 */
interface RequestDetailContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetRequestSuccess(Request request);

        void onGetRequestFailure(String message);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getRequest(int requestId);
    }
}
