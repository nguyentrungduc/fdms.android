package com.framgia.fdms.screen.new_selection;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.framgia.fdms.BR;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.utils.navigator.Navigator;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Exposes the data to be used in the StatusSelection screen.
 */

public class StatusSelectionViewModel extends BaseObservable
    implements StatusSelectionContract.ViewModel {
    private static final String BUNDLE_DATA = "BUNDLE_DATA";
    private StatusSelectionContract.Presenter mPresenter;
    private StatusSelectionAdapter mAdapter;
    private AppCompatActivity mActivity;
    private Navigator mNavigator;

    public StatusSelectionViewModel(AppCompatActivity activity) {
        mAdapter = new StatusSelectionAdapter(new ArrayList<Status>());
        mActivity = activity;
        mNavigator = new Navigator(activity);
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
    public void setPresenter(StatusSelectionContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getData("");
    }

    public void onSelectedItem(Status data) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_DATA, data);
        intent.putExtras(bundle);
        mNavigator.finishActivityWithResult(intent, RESULT_OK);
    }

    @Bindable
    public StatusSelectionAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(StatusSelectionAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Bindable
    public AppCompatActivity getActivity() {
        return mActivity;
    }

    public void setActivity(AppCompatActivity activity) {
        mActivity = activity;
        notifyPropertyChanged(BR.activity);
    }

    @Override
    public void onSearch(String query) {
        mPresenter.getData(query);
    }

    @Override
    public void onGetDataSuccess(List data) {
        mAdapter.updateData(data);
    }

    @Override
    public void onGetDataFailed(String msg) {
        mNavigator.showToast(msg);
    }
}
