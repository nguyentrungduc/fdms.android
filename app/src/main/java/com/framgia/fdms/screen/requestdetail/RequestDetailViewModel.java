package com.framgia.fdms.screen.requestdetail;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.screen.requestdetail.information.RequestInformationFragment;
import com.framgia.fdms.screen.requestdetail.listdeviceassignment.ListDeviceAssignmentFragment;
import com.framgia.fdms.utils.navigator.Navigator;
import java.util.ArrayList;
import java.util.List;

/**
 * Exposes the data to be used in the Devicedetail screen.
 */

public class RequestDetailViewModel extends BaseObservable
    implements RequestDetailContract.ViewModel {

    private RequestDetailContract.Presenter mPresenter;
    private RequestDetailPagerAdapter mAdapter;
    private Context mContext;
    private AppCompatActivity mActivity;
    private Request mRequest;
    private Navigator mNavigator;
    private ObservableField<Integer> mProgressBarVisibility = new ObservableField<>();

    public RequestDetailViewModel(AppCompatActivity activity, Request request,
        Navigator navigator) {
        mActivity = activity;
        mContext = mActivity.getApplicationContext();
        mRequest = request;
        mNavigator = navigator;
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(RequestInformationFragment.newInstance(mRequest));
        fragments.add(ListDeviceAssignmentFragment.newInstance(mRequest));
        mAdapter = new RequestDetailPagerAdapter(mContext, mActivity.getSupportFragmentManager(),
            fragments);
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
    public void setPresenter(RequestDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public ObservableField<Integer> getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public RequestDetailPagerAdapter getAdapter() {
        return mAdapter;
    }

    public AppCompatActivity getActivity() {
        return mActivity;
    }

    public void onClickArrowBack() {
        mNavigator.finishActivity();
    }
}
