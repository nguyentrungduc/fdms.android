package com.framgia.fdms.screen.request.requestmanager.select.assignee;

import android.content.Context;
import android.content.Intent;

import com.framgia.fdms.data.source.AssigneeDataSource;
import com.framgia.fdms.data.source.AssigneeRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.AssigneeRemoteDataSource;
import com.framgia.fdms.screen.baseselection.BaseSelectionActivity;
import com.framgia.fdms.screen.baseselection.BaseSelectionContract;

/**
 * StatusSelection Screen.
 */
public class SelectAssigneeActivity extends BaseSelectionActivity {

    private int mDeviceStatus;

    public static Intent getInstance(Context context) {
        return new Intent(context, SelectAssigneeActivity.class);
    }

    @Override
    public void getDataFromIntent() {
    }

    @Override
    public BaseSelectionContract.Presenter getPresenter() {
        AssigneeDataSource deviceRepository = AssigneeRepository
                .getInstance(AssigneeRemoteDataSource.getInstance(FDMSServiceClient.getInstance()));
        return new SelectAssignPresenter(mViewModel, deviceRepository);
    }
}
