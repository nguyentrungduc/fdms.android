package com.framgia.fdms.data.source;

import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.api.request.RegisterRequest;
import io.reactivex.Observable;

/**
 * Created by levutantuan on 4/4/17.
 */
public class UserDataSource {
    public interface LocalDataSource {
        Observable<User> getCurrentUser();

        void logout();
    }

    public interface RemoteDataSource {
        Observable<Respone<User>> login(String userName, String passWord);

        Observable<Respone<User>> loginWsm(String email);

        Observable<User> register(RegisterRequest request);

        Observable<Respone<User>> updateUserProfile(int userId, String gender, String address,
            String birthday);
    }
}
