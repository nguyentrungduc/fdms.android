package com.framgia.fdms.data.source.remote;

import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.UserDataSource;
import com.framgia.fdms.data.source.api.request.RegisterRequest;
import com.framgia.fdms.data.source.api.service.FDMSApi;
import com.google.gson.Gson;

import io.reactivex.Observable;

/**
 * Created by levutantuan on 4/4/17.
 */

public class UserRemoteDataSource implements UserDataSource.RemoteDataSource {
    private FDMSApi mFDMSApi;

    private final static String INVENTORY_USER = "{\n" +
            "  \"name\": \" Hoang Thu Hien\",\n" +
            "  \"email\": \"hoang.thu.hien@framgia.com\",\n" +
            "  \"address\": null,\n" +
            "  \"created_by\": null,\n" +
            "  \"updated_by\": null,\n" +
            "  \"avatar\": \"http://edev.framgia.vn//assets/user_avatar_default-bc6c6c40940226d6cf0c35571663cd8d231a387d5ab1921239c2bd19653987b2.png\",\n" +
            "  \"gender\": \"male\",\n" +
            "  \"branch\": \"Ha Noi\",\n" +
            "  \"card_number\": \"0004389087\",\n" +
            "  \"birthday\": \"1993-02-24\",\n" +
            "  \"employee_code\": \"B123512\",\n" +
            "  \"contract_date\": \"2016-07-27\",\n" +
            "  \"start_working_date\": \"2016-06-15\",\n" +
            "  \"resign_date\": null,\n" +
            "  \"start_probation_date\": \"2016-06-15\",\n" +
            "  \"end_probation_date\": \"2016-07-25\",\n" +
            "  \"status\": \"working\",\n" +
            "  \"role\": 6,\n" +
            "  \"token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo1MzMsImV4cCI6MTUxMzMwMTUyNSwiaXNzIjoiZnJhbWdpYSIsImF1ZCI6ImNsaWVudCJ9.GS6FvoV77VYtW-KWNuxXbDsYA4iJFPxTI5tlA3jCD50\"\n" +
            "}";

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
    public Observable<Respone<User>> loginInventory() {
        User user = new Gson().fromJson(INVENTORY_USER, User.class);
        Respone<User> response = new Respone<>();
        response.setData(user);
        response.setStatus(200);
        response.setMessage("");
        response.setError(false);
        return Observable.just(response);
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
