package com.framgia.fdms.data.source.remote;

import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.UserDataSource;
import com.framgia.fdms.data.source.api.request.RegisterRequest;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import io.reactivex.Observable;

/**
 * Created by levutantuan on 4/4/17.
 */

public class UserRemoteDataSource implements UserDataSource.RemoteDataSource {
    private FDMSApi mFDMSApi;

    public UserRemoteDataSource(FDMSApi FDMSApi) {
        mFDMSApi = FDMSApi;
    }

    @Override
    public Observable<Respone<User>> login(String userName, String passWord) {
        return mFDMSApi.login(userName, passWord);
    }

    @Override
    public Observable<Respone<User>> loginWsm(String userName, String passWord) {
        return mFDMSApi.loginWsm(userName, passWord);
    }

    @Override
    public Observable<User> register(RegisterRequest request) {
        // TODO: 4/4/17 replace by call API later
        return Observable.just(null);
    }

    @Override
    public Observable<Respone<User>> updateUserProfile(int userId, String gender, String address,
        String birthday) {
        return mFDMSApi.updateUserProfile(userId, gender, address, birthday);
    }
}
