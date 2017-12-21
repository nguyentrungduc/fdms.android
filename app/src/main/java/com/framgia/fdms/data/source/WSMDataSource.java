package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.WSMResponse;
import com.framgia.fdms.data.model.WSMUserResponse;

import io.reactivex.Observable;

/**
 * Created by doan.van.toan on 12/21/17.
 */

public interface WSMDataSource {
    Observable<WSMResponse<WSMUserResponse>> loginByWsmApi(String userName,
                                                           String passWord);
}
