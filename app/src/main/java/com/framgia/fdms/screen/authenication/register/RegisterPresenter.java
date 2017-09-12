package com.framgia.fdms.screen.authenication.register;

import android.text.TextUtils;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import com.framgia.fdms.data.source.api.request.RegisterRequest;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Listens to user actions from the UI ({@link RegisterActivity}), retrieves the data and updates
 * the UI as required.
 */
final class RegisterPresenter implements RegisterContract.Presenter {

    private final RegisterContract.ViewModel mViewModel;
    private CompositeDisposable mCompositeSubscription;
    private UserRepository mUserRepository;
    private RegisterRequest mRequest;

    public RegisterPresenter(RegisterContract.ViewModel viewModel, UserRepository repository) {
        mViewModel = viewModel;
        mUserRepository = repository;
        mRequest = new RegisterRequest();
        mCompositeSubscription = new CompositeDisposable();
    }

    @Override
    public void onStart() {
        //TODO dev later
    }

    @Override
    public void onStop() {
        mCompositeSubscription.clear();
    }

    @Override
    public void register(RegisterRequest request) {
        if (!validateDataInput(request)) {
            return;
        }
        Disposable subscription = mUserRepository.register(mRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<User>() {
                @Override
                public void accept(User user) throws Exception {
                    mViewModel.onRegisterSuccess();
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onRegisterError();
                }
            });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public boolean validateDataInput(RegisterRequest request) {
        boolean isValid = true;
        if (TextUtils.isEmpty(request.getUserName())) {
            isValid = false;
            mViewModel.onInputUserNameError();
        }
        if (TextUtils.isEmpty(request.getPassWord())) {
            isValid = false;
            mViewModel.onInputPasswordError();
        }
        if (TextUtils.isEmpty(request.getConfirmPassword())) {
            isValid = false;
            mViewModel.onInputConfirmPasswordError();
        }
        if (TextUtils.isEmpty(request.getFirstName())) {
            isValid = false;
            mViewModel.onInputFirstnameError();
        }
        if (TextUtils.isEmpty(request.getLastName())) {
            isValid = false;
            mViewModel.onInputLastnameError();
        }
        if (TextUtils.isEmpty(request.getAddress())) {
            isValid = false;
            mViewModel.onInputAddressError();
        }
        if (TextUtils.isEmpty(request.getRole())) {
            isValid = false;
            mViewModel.onInputRoleError();
        }
        if (TextUtils.isEmpty(request.getDepartment())) {
            isValid = false;
            mViewModel.onInputDepartmentError();
        }
        return isValid;
    }
}
