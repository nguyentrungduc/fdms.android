package com.framgia.fdms.data.source.remote;

import com.framgia.fdms.data.source.api.service.WSMApi;

public abstract class BaseWSMRemoteDataSource {
    protected WSMApi mWSMApi;

    public BaseWSMRemoteDataSource(WSMApi api) {
        mWSMApi = api;
    }
}
