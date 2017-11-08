package com.framgia.fdms.screen.requestcreation.requestfor;

import android.content.Context;
import android.content.Intent;

import com.framgia.fdms.FDMSApplication;
import com.framgia.fdms.data.source.AssigneeDataSource;
import com.framgia.fdms.data.source.AssigneeRepository;
import com.framgia.fdms.data.source.StatusRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.AssigneeRemoteDataSource;
import com.framgia.fdms.data.source.remote.StatusRemoteDataSource;
import com.framgia.fdms.screen.baseselection.BaseSelectionActivity;
import com.framgia.fdms.screen.baseselection.BaseSelectionContract;

/**
 * StatusSelection Screen.
 */
public class SelectRequestForActivity extends BaseSelectionActivity {

    public static Intent getInstance(Context context) {
        return new Intent(context, SelectRequestForActivity.class);
    }

    @Override
    public void getDataFromIntent() {
    }

    @Override
    public BaseSelectionContract.Presenter getPresenter() {
        StatusRepository statusRepository = new StatusRepository(
                new StatusRemoteDataSource(FDMSServiceClient.getInstance()));
        return new SelectRequestForPresenter(mViewModel, statusRepository);
    }
}
