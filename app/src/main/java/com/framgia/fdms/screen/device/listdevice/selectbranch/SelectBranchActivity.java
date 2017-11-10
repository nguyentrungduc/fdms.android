package com.framgia.fdms.screen.device.listdevice.selectbranch;

import android.content.Context;
import android.content.Intent;

import com.framgia.fdms.data.source.BranchRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.BranchRemoteDataSource;
import com.framgia.fdms.screen.baseselection.BaseSelectionActivity;
import com.framgia.fdms.screen.baseselection.BaseSelectionContract;

/**
 * StatusSelection Screen.
 */
public class SelectBranchActivity extends BaseSelectionActivity {
    public static Intent getInstance(Context context) {
        return new Intent(context, SelectBranchActivity.class);
    }

    @Override
    public void getDataFromIntent() {

    }

    @Override
    public BaseSelectionContract.Presenter getPresenter() {
        BranchRepository branchRepository = new BranchRepository(
                new BranchRemoteDataSource(FDMSServiceClient.getInstance()));
        return new SelectBranchPresenter(mViewModel, branchRepository);
    }
}
