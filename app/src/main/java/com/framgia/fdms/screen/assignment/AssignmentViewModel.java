package com.framgia.fdms.screen.assignment;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.screen.profile.chooseexport.ChooseExportActivity;

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

    public AssignmentViewModel(AppCompatActivity activity) {
        mActivity = activity;
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

    @Override
    public void onAddItemClick() {
        hideSoftKeyboard(mActivity);
        // TODO: 12/09/2017  mAdapter.addItem(request);
    }

    @Override
    public void onSaveClick() {
        // TODO: 19/06/2017
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
}
