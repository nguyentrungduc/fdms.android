package com.framgia.fdms.screen.devicecreation.selectdevicestatus;

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

    private static final String EXTRA_DEVICE_STATUS = "EXTRA_DEVICE_STATUS";
    private int mDeviceStatus;

    public static Intent getInstance(Context context, int deviceStatus) {
        Intent intent = new Intent(context, SelectDeviceStatusActivity.class);
        intent.putExtra(EXTRA_DEVICE_STATUS, deviceStatus);
        return intent;
    }

    @Override
    public void getDataFromIntent() {
        mDeviceStatus = getIntent().getExtras().getInt(EXTRA_DEVICE_STATUS, OUT_OF_INDEX);
    }

    @Override
    public BaseSelectionContract.Presenter getPresenter() {
        DeviceRepository deviceRepository = new DeviceRepository(
                new DeviceRemoteDataSource(FDMSServiceClient.getInstance()));
        return new SelectDeviceStatusPresenter(mViewModel, mDeviceStatus, deviceRepository);
    }
}
