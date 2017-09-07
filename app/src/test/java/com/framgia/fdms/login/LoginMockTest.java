package com.framgia.fdms.login;

import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.screen.authenication.login.LoginPresenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import rx.Observable;
import rx.functions.Func1;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
/**
 * Created by beepi on 18/07/2017.
 */

/**
 * make test for class {@link LoginPresenter}
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginMockTest {
    @Mock
    private LoginPresenter mPresenter;
    @Mock
    private UserRepository mUserRepository;

    @Test(expected = BaseException.class)
    public void testLogin() {
        final Respone<User> userRespond = new Respone<User>();
        userRespond.setData(new User());
        /**
         * stub usename and password are valid
         */
        String usename = "nguyen.thi.minh.ngoc@framgia.com";
        String password = "framgia";
        when(mUserRepository.login(anyString(), anyString()))
            .thenReturn(Observable.just(userRespond));
        /**
         * test callback respond
         */
        Observable<Respone<User>> observable = mUserRepository.login(usename, password);
        assertNotNull(observable);
        observable.flatMap(new Func1<Respone<User>, Observable<User>>() {
            @Override
            public Observable<User> call(Respone<User> userRespone) {
                assertNotNull(userRespond.getData());
                userRespond.getMessage();
                return null;
            }
        });
        /**
         * stub invalid username and password
         */
        password = "framgi";
        doThrow(BaseException.class).when(mPresenter).login(usename, password);
        mPresenter.login(usename, password);
    }

    @Test(expected = NullPointerException.class)
    public void testValidateInput() {
        String usename = null, password = null;
        /**
         * stub exception when input is null
         */
        doThrow(new NullPointerException()).when(mPresenter).login(usename, password);
        mPresenter.login(usename, password);
    }
}
