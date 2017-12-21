package com.framgia.fdms;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.crashlytics.android.Crashlytics;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.source.api.service.FDMSServiceClient;
import com.framgia.fdms.data.source.api.service.WSMServiceClient;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Age on 4/3/2017.
 */
public class FDMSApplication extends Application {
    public static Device sUpdatedDevice;
    private static FDMSApplication sInstant;

    public static FDMSApplication getInstant() {
        return sInstant;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        FDMSServiceClient.initialize(this);
        WSMServiceClient.initialize(this);
        sInstant = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
