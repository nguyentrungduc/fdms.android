package com.framgia.fdms.screen.device.listdevice.selectvendor;

import android.content.Context;
import android.content.Intent;

import com.framgia.fdms.data.source.VendorRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.VendorRemoteDataSource;
import com.framgia.fdms.screen.baseselection.BaseSelectionActivity;
import com.framgia.fdms.screen.baseselection.BaseSelectionContract;

/**
 * StatusSelection Screen.
 */
public class SelectVendorActivity extends BaseSelectionActivity {
    public static Intent getInstance(Context context) {
        return new Intent(context, SelectVendorActivity.class);
    }

    @Override
    public void getDataFromIntent() {

    }

    @Override
    public BaseSelectionContract.Presenter getPresenter() {
        VendorRepository vendorRepository = new VendorRepository(
                new VendorRemoteDataSource(FDMSServiceClient.getInstance()));
        return new SelectVendorPresenter(mViewModel, vendorRepository);
    }
}
