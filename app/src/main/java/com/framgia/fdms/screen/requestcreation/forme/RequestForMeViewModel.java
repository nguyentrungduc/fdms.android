package com.framgia.fdms.screen.requestcreation.forme;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;
import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.AssigneeUser;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.api.request.RequestCreatorRequest;
import com.framgia.fdms.screen.requestcreation.assignee.SelectAssigneeRequestActivity;
import com.framgia.fdms.utils.Utils;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fdms.data.anotation.Permission.BO_MANAGER;
import static com.framgia.fdms.data.anotation.Permission.NORMAL_USER;
import static com.framgia.fdms.screen.selection.SelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.NONE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_ASSIGNEE;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_RELATIVE;

/**
 * Exposes the data to be used in the Requestcreation screen.
 */

public class RequestForMeViewModel extends BaseObservable
        implements RequestForMeContract.ViewModel, AdapterView.OnItemSelectedListener {

    private RequestForMeContract.Presenter mPresenter;
    private AppCompatActivity mActivity;
    private String mRequestTitle;
    private String mRequestDescription;
    private Status mAssignee;
    private RequestCreatorRequest mRequest;
    private Status mDefaultAssignee;
    private String mExpectedDate;
    private Status mGroup;
    private ArrayAdapter<Status> mAdapter;
    private Context mContext;
    private String mTitleError;
    private String mDescriptionError;
    private String mDateError;
    private int mProgressBarVisibility = View.GONE;
    private boolean isAllowAddAssignee;
    private boolean isAllowAddRequestFor;
    private boolean isGroupVisible;

    public RequestForMeViewModel(AppCompatActivity activity) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
        mRequest = new RequestCreatorRequest();
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
    public void setPresenter(RequestForMeContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getCurrentUser();
    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }

    @Override
    public void onCreateRequestClick() {
        mPresenter.registerRequest(mRequest);
    }

    @Override
    public void onLoadError(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressbar() {
        setProgressBarVisibility(View.GONE);
    }

    @Override
    public void showProgressbar() {
        setProgressBarVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        switch (requestCode) {
            case REQUEST_RELATIVE:
                AssigneeUser user = bundle.getParcelable(BUNDLE_DATA);
                updateDefaultAssignee();
                setAdapter(
                        new ArrayAdapter<Status>(mContext, R.layout.item_group, user.getGroups()));
                break;
            case REQUEST_ASSIGNEE:
                Status status = bundle.getParcelable(BUNDLE_DATA);
                setAssignee(status);
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetRequestSuccess(Request request) {
        Toast.makeText(mContext, R.string.msg_create_request_success, Toast.LENGTH_SHORT).show();
        finnishActivityWithResult(request);
    }

    public void finnishActivityWithResult(Request request) {
        Intent intent = new Intent();
        intent.putExtra(BUNDLE_DATA, request);
        mActivity.setResult(RESULT_OK, intent);
        mActivity.finish();
    }

    @Override
    public void onInputTitleError() {
        mTitleError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.titleError);
    }

    @Override
    public void onInputDescriptionError() {
        mDescriptionError = mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.descriptionError);
    }

    @Override
    public void onGetUserSuccess(User user) {
        boolean isAllowAddAssignee = user.getRole() == BO_MANAGER;
        setAllowAddAssignee(isAllowAddAssignee);
        setAllowAddRequestFor(isAllowAddRequestFor);
        setAssignee(new Status(OUT_OF_INDEX, NONE));

        boolean isAllowChooseGroup = user.getRole() == NORMAL_USER;
        setGroupVisible(isAllowChooseGroup);

        setAdapter(new ArrayAdapter<Status>(mContext, R.layout.item_group, user.getGroups()));
    }

    @Override
    public void onGetDefaultAssignSuccess(Status assignee) {
        mDefaultAssignee = assignee;
    }

    @Bindable
    public String getRequestTitle() {
        return mRequestTitle;
    }

    public void setRequestTitle(String requestTitle) {
        mRequest.setTitle(requestTitle);
        mRequestTitle = requestTitle;
    }

    @Bindable
    public String getRequestDescription() {
        return mRequestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        mRequest.setDescription(requestDescription);
        mRequestDescription = requestDescription;
    }

    @Bindable
    public Status getAssignee() {
        return mAssignee;
    }

    public void setAssignee(Status assignee) {
        mAssignee = assignee;
        mRequest.setAssignee(assignee.getId());
        notifyPropertyChanged(BR.assignee);
    }

    @Bindable
    public String getTitleError() {
        return mTitleError;
    }

    @Bindable
    public String getDescriptionError() {
        return mDescriptionError;
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
    public boolean isGroupVisible() {
        return isGroupVisible;
    }

    public void setGroupVisible(boolean visible) {
        isGroupVisible = visible;
        notifyPropertyChanged(BR.groupVisible);
    }

    public void updateDefaultAssignee() {
        if (mAssignee.getId() < 0 && mDefaultAssignee != null) {
            setAssignee(mDefaultAssignee);
        }
    }

    @Bindable
    public String getExpectedDate() {
        return mExpectedDate;
    }

    public void setExpectedDate(String date) {
        mExpectedDate = date;
        mRequest.setExpectedDate(date);
        notifyPropertyChanged(BR.expectedDate);
    }

    @Bindable
    public String getDateError() {
        return mDateError;
    }

    @Override
    public void onInputDateError() {
        mDateError = mContext.getString(R.string.msg_error_date);
        notifyPropertyChanged(BR.dateError);
    }

    @Override
    public void onInputDateEmpty(){
        mDateError=mContext.getString(R.string.msg_error_user_name);
        notifyPropertyChanged(BR.dateError);
    }

    public void onClickAssignee() {
        mActivity.startActivityForResult(SelectAssigneeRequestActivity.getInstance(mContext),
                REQUEST_ASSIGNEE);
    }

    public void onClickExpectedDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                            int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        setExpectedDate(Utils.dateToString(calendar.getTime()));
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    @Bindable
    public ArrayAdapter<Status> getAdapter() {
        return mAdapter;
    }

    public void setAdapter(ArrayAdapter<Status> adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setGroup((Status) parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        setGroup((Status) parent.getItemAtPosition(0));
    }

    @Bindable
    public boolean isAllowAddRequestFor() {
        return isAllowAddRequestFor;
    }

    public void setAllowAddRequestFor(boolean allowAddRequestFor) {
        isAllowAddRequestFor = allowAddRequestFor;
        notifyPropertyChanged(BR.allowAddRequestFor);
    }

    @Bindable
    public Status getGroup() {
        return mGroup;
    }

    public void setGroup(Status group) {
        mGroup = group;
        mRequest.setGroupId(group.getId());
        notifyPropertyChanged(BR.group);
    }

    @Bindable
    public boolean isAllowAddAssignee() {
        return isAllowAddAssignee;
    }

    public void setAllowAddAssignee(boolean allowAddAssignee) {
        isAllowAddAssignee = allowAddAssignee;
        notifyPropertyChanged(BR.allowAddAssignee);
    }
}
