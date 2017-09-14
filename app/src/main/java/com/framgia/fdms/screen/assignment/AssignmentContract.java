package com.framgia.fdms.screen.assignment;

import android.content.Intent;
import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.AssignmentRequest;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface AssignmentContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onAddItemClick();

        void onSaveClick();

        void onLoadError(String msg);

        void onGetRequestSuccess(Request request);

        void openChooseExportActivity();

        void openChooseExportActivitySuccess(User user);

        void onChooseExportActivityFailed();

        void onAssignmentSuccess(Request request);

        void onGetDeviceGroupsSuccess(List<Status> statuses);

        void onGetCategoriesSuccess(List<Status> statuses);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void registerAssignment(AssignmentRequest request);

        void getRequest(int requestId);

        void chooseExportActivity();

        void getDeviceGroups();

        void getCategoriesByDeviceGroupId(int deviceGroupId);
    }
}
