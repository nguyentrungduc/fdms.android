package com.framgia.fdms.screen.device.mydevice.mydevicedetail;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import com.framgia.fdms.BR;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.utils.navigator.Navigator;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Exposes the data to be used in the MyDeviceDetail screen.
 */

public class MyDeviceDetailViewModel extends BaseObservable
    implements MyDeviceDetailContract.ViewModel {

    private static final String TAG = "MyDeviceDetailViewModel";

    private MyDeviceDetailContract.Presenter mPresenter;
    private int mProgressVisibility = GONE;
    private Navigator mNavigator;

    public MyDeviceDetailViewModel(Fragment fragment) {
        mNavigator = new Navigator(fragment);
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
    public void setPresenter(MyDeviceDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetDataFailure(String msg) {
        mNavigator.showToast(msg);
    }

    @Override
    public void hideProgress() {
        setProgressVisibility(GONE);
    }

    @Override
    public void showProgress() {
        setProgressVisibility(VISIBLE);
    }

    @Override
    public void onGetDeviceSuccess(List<DeviceUsingHistory> deviceUsingHistories) {
        // TODO: 9/22/2017
    }

    @Bindable
    public int getProgressVisibility() {
        return mProgressVisibility;
    }

    public void setProgressVisibility(int progressVisibility) {
        mProgressVisibility = progressVisibility;
        notifyPropertyChanged(BR.progressVisibility);
    }
}
