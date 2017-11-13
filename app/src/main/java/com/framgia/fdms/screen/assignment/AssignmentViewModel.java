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
import com.framgia.fdms.data.model.AssignmentItemRequest;
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
import static com.framgia.fdms.screen.selection.SelectionType.CATEGORY;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_GROUP;
import static com.framgia.fdms.screen.selection.SelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_DEVICES;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_SUCCESS;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_CATEGORIES;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_DEVICE;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_DEVICE_GROUPS;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_USER_BORROW;
import static com.framgia.fdms.utils.Utils.hideSoftKeyboard;

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
    private Status mDeviceGroup;
    private Status mCategory;
    private Device mDevice;
    private Navigator mNavigator;
    private String mDeviceGroupHint;
    private String mCategoryHint;
    private String mDeviceHint;
    @AssignmentType
    private int mAssignmentType;
    private Status mStaff;

    public AssignmentViewModel(AppCompatActivity activity) {
        mActivity = activity;
        mNavigator = new Navigator(activity);
        mContext = activity.getApplicationContext();
        mAdapter = new AssignmentAdapter(mContext, this);
        setDeviceGroupHint(mContext.getString(R.string.title_assign_group));
        setCategoryHint(mContext.getString(R.string.title_assign_cagegory));
        setDeviceHint(mContext.getString(R.string.title_assign_device));
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

    public void onShowDeviceGroups() {
        mNavigator.startActivityForResult(SelectionActivity.getInstance(mContext, DEVICE_GROUP),
            REQUEST_DEVICE_GROUPS);
    }

    public void onShowCategories() {
        if (mDeviceGroup == null) {
            return;
        }
        mNavigator.startActivityForResult(
            SelectionActivity.getInstance(mContext, CATEGORY, mDeviceGroup.getId()),
            REQUEST_CATEGORIES);
    }

    public void onShowDevices() {
        if (mCategory == null) {
            return;
        }
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
            case REQUEST_DEVICE_GROUPS:
                status = bundle.getParcelable(BUNDLE_DATA);
                if (status != null) {
                    setDeviceGroup(status);
                    resetCategory();
                    resetDevice();
                }
                break;
            case REQUEST_CATEGORIES:
                status = bundle.getParcelable(BUNDLE_DATA);
                if (status != null) {
                    setCategory(status);
                    resetDevice();
                }
                break;
            case REQUEST_DEVICE:
                Device device = bundle.getParcelable(BUNDLE_DEVICES);
                if (device != null) {
                    setDevice(device);
                }
                break;

            case REQUEST_USER_BORROW:
                status = bundle.getParcelable(BUNDLE_DATA);
                if (status != null && status.getId() != OUT_OF_INDEX) {
                    setStaff(status);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onAddItemClick() {
        hideSoftKeyboard(mActivity);
        if (!mPresenter.validateAddItem(mCategory, mDevice, mDeviceGroup)) {
            return;
        }
        mAdapter.addItem(new AssignmentItemRequest.Builder().deviceId(mDevice.getId())
            .deviceCategoryId(mCategory.getId())
            .categoryGroupId(mDeviceGroup.getId())
            .deviceCategoryName(mCategory.getName())
            .deviceCategoryGroupName(mDeviceGroup.getName())
            .deviceCode(mDevice.getDeviceCode())
            .create());
        resetDeviceGroup();
        resetCategory();
        resetDevice();
    }

    @Override
    public void onSaveClick() {
        switch (mAssignmentType) {
            default:
            case AssignmentType.ASSIGN_BY_REQUEST:
                AssignmentRequest assignmentRequest =
                    new AssignmentRequest(mRequest.getId(), mRequest.getAssigneeId(),
                        mRequest.getDescription(), mAdapter.getData());
                mPresenter.registerAssignment(assignmentRequest);
                break;
            case AssignmentType.ASSIGN_BY_NEW_MEMBER:
                mPresenter.registerAssignment(mStaff, mAdapter.getData());
                break;
        }
    }

    public void onChooseStaffClick() {
        mNavigator.startActivityForResult(
            SelectionActivity.getInstance(mActivity.getApplicationContext(),
                SelectionType.USER_BORROW), REQUEST_USER_BORROW);
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

    private void resetDeviceGroup() {
        mDeviceGroup = null;
        setDeviceGroupHint(mContext.getString(R.string.title_request_assignment));
    }

    private void resetCategory() {
        mCategory = null;
        setCategoryHint(mContext.getString(R.string.title_btn_category));
    }

    private void resetDevice() {
        mDevice = null;
        setDeviceHint(mContext.getString(R.string.title_device_code));
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
    public Status getDeviceGroup() {
        return mDeviceGroup;
    }

    public void setDeviceGroup(Status deviceGroup) {
        mDeviceGroup = deviceGroup;
        notifyPropertyChanged(BR.deviceGroup);
    }

    @Bindable
    public Status getCategory() {
        return mCategory;
    }

    public void setCategory(Status category) {
        mCategory = category;
        notifyPropertyChanged(BR.category);
    }

    @Bindable
    public Device getDevice() {
        return mDevice;
    }

    public void setDevice(Device device) {
        mDevice = device;
        notifyPropertyChanged(BR.device);
    }

    @Bindable
    public String getDeviceGroupHint() {
        return mDeviceGroupHint;
    }

    public void setDeviceGroupHint(String deviceGroupHint) {
        mDeviceGroupHint = deviceGroupHint;
        notifyPropertyChanged(BR.deviceGroupHint);
    }

    @Bindable
    public String getCategoryHint() {
        return mCategoryHint;
    }

    public void setCategoryHint(String categoryHint) {
        mCategoryHint = categoryHint;
        notifyPropertyChanged(BR.categoryHint);
    }

    @Bindable
    public String getDeviceHint() {
        return mDeviceHint;
    }

    public void setDeviceHint(String deviceHint) {
        mDeviceHint = deviceHint;
        notifyPropertyChanged(BR.deviceHint);
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
}
