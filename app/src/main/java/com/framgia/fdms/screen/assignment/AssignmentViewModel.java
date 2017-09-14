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
import com.framgia.fdms.screen.profile.chooseexport.ChooseExportActivity;
import com.framgia.fdms.screen.selection.StatusSelectionActivity;
import com.framgia.fdms.screen.selection.StatusSelectionType;
import com.framgia.fdms.utils.navigator.Navigator;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fdms.screen.selection.StatusSelectionAdapter.FIRST_INDEX;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_DEVICES;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_STATUE;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_SUCCESS;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_CATEGORIES;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_DEVICE;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_DEVICE_GROUPS;
import static com.framgia.fdms.utils.Utils.hideSoftKeyboard;

/**
 * Exposes the data to be used in the Assignment screen.
 */
public class AssignmentViewModel extends BaseObservable implements AssignmentContract.ViewModel {
    private static final int NO_DEVICE = -2;
    private AssignmentContract.Presenter mPresenter;
    private AppCompatActivity mActivity;
    private int mProgressBarVisibility = View.GONE;
    private Request mRequest;
    private AssignmentAdapter mAdapter;
    private Context mContext;
    private List<Status> mDeviceGroups;
    private List<Status> mCategories;
    private Status mDeviceGroup;
    private Status mCategory;
    private int mCategoryId = NO_DEVICE;
    private Device mDevice;
    private Navigator mNavigator;
    private String mDeviceGroupHint;
    private String mCategoryHint;
    private String mDeviceHint;

    public AssignmentViewModel(AppCompatActivity activity) {
        mActivity = activity;
        mNavigator = new Navigator(activity);
        mContext = activity.getApplicationContext();
        mAdapter = new AssignmentAdapter(mContext, this);
        setDeviceGroupHint(mContext.getString(R.string.title_request_assignment));
        setCategoryHint(mContext.getString(R.string.title_btn_category));
        setDeviceHint(mContext.getString(R.string.title_device_code));
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
        if (mDeviceGroups == null) {
            return;
        }
        mNavigator.startActivityForResult(
            StatusSelectionActivity.getInstance(mContext, null, mDeviceGroups,
                StatusSelectionType.STATUS), REQUEST_DEVICE_GROUPS);
    }

    public void onShowCategories() {
        if (mCategories == null) {
            return;
        }
        mNavigator.startActivityForResult(
            StatusSelectionActivity.getInstance(mContext, null, mCategories,
                StatusSelectionType.STATUS), REQUEST_CATEGORIES);
    }

    public void onShowDevices() {
        if (mCategoryId == NO_DEVICE) {
            return;
        }
        mNavigator.startActivityForResult(
            DeviceSelectionActivity.getInstance(mContext, mCategoryId), REQUEST_DEVICE);
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
                status = bundle.getParcelable(BUNDLE_STATUE);
                if (status != null) {
                    setCategoryHint(mContext.getString(R.string.title_btn_category));
                    setDeviceHint(mContext.getString(R.string.title_device_code));
                    mCategories = null;
                    mCategory = null;
                    mDevice = null;
                    mCategoryId = OUT_OF_INDEX;
                    setDeviceGroup(status);
                    mPresenter.getCategoriesByDeviceGroupId(status.getId());
                }
                break;
            case REQUEST_CATEGORIES:
                status = bundle.getParcelable(BUNDLE_STATUE);
                if (status != null) {
                    setDeviceHint(mContext.getString(R.string.title_device_code));
                    setCategory(status);
                    mCategoryId = status.getId();
                }
                break;
            case REQUEST_DEVICE:
                Device device = bundle.getParcelable(BUNDLE_DEVICES);
                if (device != null) {
                    setDevice(device);
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
    }

    @Override
    public void onSaveClick(View view) {
        FloatingActionsMenu fapMenu = (FloatingActionsMenu) view.getParent();
        fapMenu.toggle();
        AssignmentRequest assignmentRequest =
            new AssignmentRequest(mRequest.getId(), mRequest.getAssigneeId(),
                mRequest.getDescription(), mAdapter.getData());
        mPresenter.registerAssignment(assignmentRequest);
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
        mPresenter.chooseExportActivity();
    }

    @Override
    public void openChooseExportActivitySuccess(User user) {
        mActivity.startActivity(ChooseExportActivity.newInstance(mContext, user));
    }

    @Override
    public void onChooseExportActivityFailed() {
        Snackbar.make(mActivity.findViewById(android.R.id.content), R.string.error_authentication,
            Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAssignmentSuccess(Request request) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SUCCESS, R.string.msg_assignment_success);
        intent.putExtras(bundle);
        mNavigator.finishActivityWithResult(intent, RESULT_OK);
    }

    @Override
    public void onGetDeviceGroupsSuccess(List<Status> statuses) {
        if (statuses == null || statuses.size() == 0) {
            setDeviceGroupHint(mContext.getString(R.string.title_no_device_group));
            return;
        }
        mDeviceGroups = statuses;
        mDeviceGroups.add(FIRST_INDEX,
            new Status(OUT_OF_INDEX, mContext.getString(R.string.action_all)));
    }

    @Override
    public void onGetCategoriesSuccess(List<Status> statuses) {
        if (statuses == null || statuses.size() == 0) {
            setCategoryHint(mContext.getString(R.string.title_no_category));
            return;
        }
        mCategories = statuses;
        if (mDeviceGroup.getId() == OUT_OF_INDEX) {
            mCategories.add(FIRST_INDEX,
                new Status(OUT_OF_INDEX, mContext.getString(R.string.action_all)));
        }
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
}
