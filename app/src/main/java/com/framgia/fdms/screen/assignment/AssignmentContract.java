package com.framgia.fdms.screen.assignment;

import android.content.Intent;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.AssignmentRequest;
import com.framgia.fdms.data.model.Device;
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

        void onSaveClick();

        void onLoadError(String msg);

        void onGetRequestSuccess(Request request);

        void openChooseExportActivity();

        void openChooseExportActivitySuccess(User user);

        void onChooseExportActivityFailed();

        void onAssignmentSuccess();

        void onError(int stringId);

        void onAssignDeviceForMeetingRoomSuccess(Status room);

        void onAssignDeviceForRequestSuccess(int id);

        void showProgress();

        void hideProgress();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void registerAssignmentForRequest(AssignmentRequest request);

        void registerAssignmentForMember(Status staff, List<Device> requests);

        void registerAssignmentForMeetingRoom(Status room, List<Device> requests);

        void getRequest(int requestId);

        void chooseExportActivity();
    }
}
