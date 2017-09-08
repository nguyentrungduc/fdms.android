package com.framgia.fdms.screen.requestcreation;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.source.api.request.RequestCreatorRequest;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface RequestCreationContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onCreateRequestClick();

        void onLoadError(String message);

        void hideProgressbar();

        void showProgressbar();

        void onGetRequestSuccess(Request request);

        void onInputTitleError();

        void onInputDescriptionError();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void registerRequest(RequestCreatorRequest request);

        boolean validateDataInput(RequestCreatorRequest request);
    }
}
