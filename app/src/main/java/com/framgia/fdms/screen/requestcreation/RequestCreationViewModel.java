package com.framgia.fdms.screen.requestcreation;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Category;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.source.api.request.RequestCreatorRequest;

import static android.app.Activity.RESULT_OK;

/**
 * Exposes the data to be used in the Requestcreation screen.
 */

public class RequestCreationViewModel extends BaseObservable
    implements RequestCreationContract.ViewModel {

    private ArrayAdapter<Category> mAdapterCategory;
    private RequestCreationContract.Presenter mPresenter;
    private AppCompatActivity mActivity;
    private String mRequestTitle;
    private String mRequestDescription;
    private RequestCreatorRequest mRequest;

    private Context mContext;
    private String mTitleError;
    private String mDescriptionError;
    private int mProgressBarVisibility = View.GONE;

    public RequestCreationViewModel(AppCompatActivity activity) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
        mRequest = new RequestCreatorRequest();
        mAdapterCategory =
            new ArrayAdapter<>(mActivity, R.layout.support_simple_spinner_dropdown_item);
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
    public void setPresenter(RequestCreationContract.Presenter presenter) {
        mPresenter = presenter;
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
    public void onGetRequestSuccess(Request request) {
        mActivity.setResult(RESULT_OK);
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
}
