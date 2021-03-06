package com.framgia.fdms.data.source.api.service;

import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.model.WSMResponse;
import com.framgia.fdms.data.model.WSMUserResponse;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by doan.van.toan on 12/21/17.
 */

public interface WSMApi {
    @POST("api/sign_in")
    Observable<WSMResponse<WSMUserResponse>> loginByWsmApi(
            @Query("sign_in[email]") String userName,
            @Query("sign_in[password]") String passWord,
            @Query("sign_in[device_id]") String deviceId);
}
