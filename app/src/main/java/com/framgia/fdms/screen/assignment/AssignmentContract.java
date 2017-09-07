package com.framgia.fdms.screen.assignment;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Request;

/**
 * This specifies the contract between the view and the presenter.
 */
interface AssignmentContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onAddItemClick();

        void onSaveClick();

        void onLoadError(String msg);

        void onGetRequestSuccess(Request request);

        void openChooseExportActivity();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void registerAssignment(Request request);

        void getRequest(int requestId);
    }
}
