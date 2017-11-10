package com.framgia.fdms.screen.device.listdevice.selectdevicestatus;

import android.content.Context;
import android.content.Intent;

import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.DeviceRemoteDataSource;
import com.framgia.fdms.screen.baseselection.BaseSelectionActivity;
import com.framgia.fdms.screen.baseselection.BaseSelectionContract;

import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * StatusSelection Screen.
 */
public class SelectDeviceStatusActivity extends BaseSelectionActivity {

    public static Intent getInstance(Context context) {
        return new Intent(context, SelectDeviceStatusActivity.class);
    }

    @Override
    public void getDataFromIntent() {
    }

    @Override
    public BaseSelectionContract.Presenter getPresenter() {
        DeviceRepository deviceRepository = new DeviceRepository(
                new DeviceRemoteDataSource(FDMSServiceClient.getInstance()));
        return new SelectDeviceStatusPresenter(mViewModel, deviceRepository);
    }
}
