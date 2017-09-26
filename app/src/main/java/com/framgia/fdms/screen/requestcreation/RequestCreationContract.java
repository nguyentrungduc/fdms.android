package com.framgia.fdms.screen.requestcreation;

import android.content.Intent;
import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.User;
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

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onGetRequestSuccess(Request request);

        void onInputTitleError();

        void onInputDescriptionError();

        void onGetUserSuccess(User user);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getCurrentUser();

        void registerRequest(RequestCreatorRequest request);

        boolean validateDataInput(RequestCreatorRequest request);
    }
}
