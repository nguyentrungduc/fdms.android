package com.framgia.fdms.screen.device.listdevice.selectmaker;

import android.content.Context;
import android.content.Intent;

import com.framgia.fdms.data.source.MarkerRepository;
import com.framgia.fdms.data.source.VendorRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.MarkerRemoteDataSource;
import com.framgia.fdms.data.source.remote.VendorRemoteDataSource;
import com.framgia.fdms.screen.baseselection.BaseSelectionActivity;
import com.framgia.fdms.screen.baseselection.BaseSelectionContract;

/**
 * StatusSelection Screen.
 */
public class SelectMakerActivity extends BaseSelectionActivity {
    public static Intent getInstance(Context context) {
        return new Intent(context, SelectMakerActivity.class);
    }

    @Override
    public void getDataFromIntent() {

    }

    @Override
    public BaseSelectionContract.Presenter getPresenter() {
        MarkerRepository markerRepository = new MarkerRepository(
                new MarkerRemoteDataSource(FDMSServiceClient.getInstance()));
        return new SelectMakerPresenter(mViewModel, markerRepository);
    }
}
