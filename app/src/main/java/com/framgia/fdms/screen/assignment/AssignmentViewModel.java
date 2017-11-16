package com.framgia.fdms.screen.assignment;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.AssignmentRequest;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.screen.deviceselection.DeviceSelectionActivity;
import com.framgia.fdms.screen.selection.SelectionActivity;
import com.framgia.fdms.screen.selection.SelectionType;
import com.framgia.fdms.utils.navigator.Navigator;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fdms.screen.selection.SelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_DEVICES;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_SUCCESS;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_DEVICE;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_MEETING_ROOM;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_USER_BORROW;

/**
 * Exposes the data to be used in the Assignment screen.
 */
public class AssignmentViewModel extends BaseObservable implements AssignmentContract.ViewModel {
    private AssignmentContract.Presenter mPresenter;
    private AppCompatActivity mActivity;
    private int mProgressBarVisibility = View.GONE;
    private Request mRequest;
    private AssignmentAdapter mAdapter;
    private Context mContext;

    private Navigator mNavigator;
    @AssignmentType
    private int mAssignmentType;

    private Status mStaff;

    private Status mMeetingRoom;

    public AssignmentViewModel(AppCompatActivity activity) {
        mActivity = activity;
        mNavigator = new Navigator(activity);
        mContext = activity.getApplicationContext();
        mAdapter = new AssignmentAdapter(mContext, this);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(AssignmentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void onAddDeviceClicked() {
        mNavigator.startActivityForResult(
                DeviceSelectionActivity.getInstance(mContext), REQUEST_DEVICE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        Bundle bundle = data.getExtras();
        Status status;
        switch (requestCode) {
            case REQUEST_DEVICE:
                Device device = bundle.getParcelable(BUNDLE_DEVICES);
                if (device != null) {
                    mAdapter.addItem(device);
                }
                break;

            case REQUEST_USER_BORROW:
                status = bundle.getParcelable(BUNDLE_DATA);
                if (status != null && status.getId() != OUT_OF_INDEX) {
                    setStaff(status);
                }
                break;
            case REQUEST_MEETING_ROOM:
                status = bundle.getParcelable(BUNDLE_DATA);
                if (status != null && status.getId() != OUT_OF_INDEX) {
                    setMeetingRoom(status);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onSaveClick() {
        switch (mAssignmentType) {
            default:
            case AssignmentType.ASSIGN_BY_REQUEST:
                AssignmentRequest assignmentRequest =
                        new AssignmentRequest(mRequest.getId(), mRequest.getAssigneeId(),
                                mRequest.getDescription(), mAdapter.getData());
                mPresenter.registerAssignmentForRequest(assignmentRequest);
                break;
            case AssignmentType.ASSIGN_BY_NEW_MEMBER:
                mPresenter.registerAssignmentForMember(mStaff, mAdapter.getData());
                break;
            case AssignmentType.ASSIGN_BY_MEETING_ROOM:
                mPresenter.registerAssignmentForMeetingRoom(mMeetingRoom, mAdapter.getData());
                break;
        }
    }

    public void onChooseStaffClick() {
        mNavigator.startActivityForResult(
                SelectionActivity.getInstance(mActivity.getApplicationContext(),
                        SelectionType.USER_BORROW), REQUEST_USER_BORROW);
    }

    public void onChooseMeetingRoomClicked() {
        mNavigator.startActivityForResult(
                SelectionActivity.getInstance(mActivity.getApplicationContext(),
                        SelectionType.MEETING_ROOM), REQUEST_MEETING_ROOM);
    }

    @Override
    public void onLoadError(String msg) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onGetRequestSuccess(Request request) {
        if (request == null) return;
        setRequest(request);
    }

    @Override
    public void openChooseExportActivity() {
        if (mAdapter.getItemCount() == 0) {
            return;
        }
        mPresenter.chooseExportActivity();
    }

    @Override
    public void openChooseExportActivitySuccess(User user) {
        if (mAdapter.getItemCount() == 0) {
            return;
        }
        // TODO: 10/9/2017   ChooseExportActivity.newInstance
    }

    @Override
    public void onChooseExportActivityFailed() {
        Snackbar.make(mActivity.findViewById(android.R.id.content), R.string.error_authentication,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAssignmentSuccess() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SUCCESS, R.string.msg_assignment_success);
        intent.putExtras(bundle);
        mNavigator.finishActivityWithResult(intent, RESULT_OK);
    }

    @Override
    public void onError(int stringId) {
        mNavigator.showToast(stringId);
    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }

    @Bindable
    public int getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public void setProgressBarVisibility(int progressBarVisibility) {
        mProgressBarVisibility = progressBarVisibility;
        notifyPropertyChanged(BR.progressBarVisibility);
    }

    @Bindable
    public Request getRequest() {
        return mRequest;
    }

    public void setRequest(Request request) {
        mRequest = request;
        notifyPropertyChanged(BR.request);
    }

    @Bindable
    public AssignmentAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(AssignmentAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Bindable
    public int getAssignmentType() {
        return mAssignmentType;
    }

    public void setAssignmentType(int assignmentType) {
        mAssignmentType = assignmentType;
        notifyPropertyChanged(BR.assignmentType);
    }

    @Bindable
    public Status getStaff() {
        return mStaff;
    }

    public void setStaff(Status staff) {
        mStaff = staff;
        notifyPropertyChanged(BR.staff);
    }

    @Bindable
    public Status getMeetingRoom() {
        return mMeetingRoom;
    }

    public void setMeetingRoom(Status meetingRoom) {
        mMeetingRoom = meetingRoom;
        notifyPropertyChanged(BR.meetingRoom);
    }
}
