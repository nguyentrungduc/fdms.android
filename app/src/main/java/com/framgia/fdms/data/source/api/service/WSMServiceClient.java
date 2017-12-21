package com.framgia.fdms.data.source.api.service;

import android.app.Application;
import android.support.annotation.NonNull;

import com.framgia.fdms.utils.Constant;

public class WSMServiceClient extends ServiceClient {
    private static WSMApi sApi;

    public static void initialize(@NonNull Application application) {
        sApi = createService(application, Constant.END_POINT_URL, WSMApi.class);
    }

    public static WSMApi getInstance() {
        if (sApi == null) {
            throw new RuntimeException("Need call method WSMServiceClient #initialize() first");
        }
        return sApi;
    }
}
