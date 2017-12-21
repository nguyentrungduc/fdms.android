package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.api.request.RegisterRequest;
import com.framgia.fdms.data.source.local.UserLocalDataSource;
import com.framgia.fdms.data.source.remote.UserRemoteDataSource;
import io.reactivex.Observable;

/**
 * Created by levutantuan on 4/4/17.
 */
public class UserRepository
    implements UserDataSource.LocalDataSource, UserDataSource.RemoteDataSource {
    private UserRemoteDataSource mUserRemoteDataSource;
    private UserLocalDataSource mUserLocalDataSource;

    public UserRepository(UserRemoteDataSource remoteDataSource) {
        mUserRemoteDataSource = remoteDataSource;
    }

    public UserRepository(UserLocalDataSource userLocalDataSource) {
        mUserLocalDataSource = userLocalDataSource;
    }

    public UserRepository(UserRemoteDataSource remoteDataSource,
        UserLocalDataSource userLocalDataSource) {
        mUserRemoteDataSource = remoteDataSource;
        mUserLocalDataSource = userLocalDataSource;
    }

    public Observable<Respone<User>> login(String userName, String passWord) {
        return mUserRemoteDataSource.login(userName, passWord);
    }

    @Override
    public Observable<Respone<User>> loginWsm(String email) {
        return mUserRemoteDataSource.loginWsm(email);
    }

    public Observable<User> register(RegisterRequest request) {
        return mUserRemoteDataSource.register(request);
    }

    @Override
    public Observable<Respone<User>> updateUserProfile(int userId, String gender, String address,
        String birthday) {
        return mUserRemoteDataSource.updateUserProfile(userId, gender, address, birthday);
    }

    public Observable<User> getCurrentUser() {
        return mUserLocalDataSource.getCurrentUser();
    }

    @Override
    public void logout() {
        mUserLocalDataSource.logout();
    }
}
