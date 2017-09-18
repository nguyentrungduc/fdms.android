package com.framgia.fdms.screen.deviceusingmanager.base;

import android.content.Intent;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.screen.deviceusingmanager.DeviceUsingManagerContract;
import java.util.List;

/**
 * Created by lamvu on 06/09/2017.
 */

public class DeviceUsingBaseViewModel implements DeviceUsingManagerContract.ViewModel {

    private DeviceUsingManagerContract.Presenter mPresenter;

    public DeviceUsingBaseViewModel() {

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
    public void setPresenter(DeviceUsingManagerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetDeviceUsingHistorySuccess(List<DeviceUsingHistory> deviceUsingHistories) {
        // TODO: 9/16/2017  
    }

    @Override
    public void onGetDeviceUsingHistoryFailed(String msg) {
        // TODO: 9/16/2017  
    }

    @Override
    public void onClearFilterClick() {
        // TODO: 9/16/2017  
    }

    @Override
    public void onChooseStatusClick() {
        // TODO: 9/16/2017  
    }

    @Override
    public void onItemDeviceClick(Device device) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
