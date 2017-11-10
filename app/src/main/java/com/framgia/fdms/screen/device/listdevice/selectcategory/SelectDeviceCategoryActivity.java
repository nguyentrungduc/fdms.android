package com.framgia.fdms.screen.device.listdevice.selectcategory;

import android.content.Context;
import android.content.Intent;

import com.framgia.fdms.data.source.CategoryRepository;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.remote.CategoryRemoteDataSource;
import com.framgia.fdms.data.source.remote.DeviceRemoteDataSource;
import com.framgia.fdms.screen.baseselection.BaseSelectionActivity;
import com.framgia.fdms.screen.baseselection.BaseSelectionContract;

import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * StatusSelection Screen.
 */
public class SelectDeviceCategoryActivity extends BaseSelectionActivity {
    public static Intent getInstance(Context context) {
        return new Intent(context, SelectDeviceCategoryActivity.class);
    }

    @Override
    public void getDataFromIntent() {

    }

    @Override
    public BaseSelectionContract.Presenter getPresenter() {
        CategoryRepository categoryRepository = new CategoryRepository(
                new CategoryRemoteDataSource(FDMSServiceClient.getInstance()));
        return new SelectDeviceCategoryPresenter(mViewModel, categoryRepository);
    }
}
