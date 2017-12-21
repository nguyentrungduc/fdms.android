package com.framgia.fdms.data.source.remote;

import android.provider.Settings;

import com.framgia.fdms.FDMSApplication;
import com.framgia.fdms.data.model.WSMResponse;
import com.framgia.fdms.data.model.WSMUserResponse;
import com.framgia.fdms.data.source.WSMDataSource;
import com.framgia.fdms.data.source.api.service.WSMApi;

import io.reactivex.Observable;

/**
 * Created by doan.van.toan on 12/21/17.
 */

public class WSMRemoteDataSource extends BaseWSMRemoteDataSource implements WSMDataSource {

    public WSMRemoteDataSource(WSMApi api) {
        super(api);
    }

    @Override
    public Observable<WSMResponse<WSMUserResponse>> loginByWsmApi(String userName,
                                                                  String passWord) {
        String deviceId = Settings.Secure.getString(
                FDMSApplication.getInstant().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return mWSMApi.loginByWsmApi(userName, passWord, deviceId);
    }
}
