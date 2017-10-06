package com.framgia.fdms.screen.device.mydevice;

import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Listens to user actions from the UI ({@link MyDeviceFragment}), retrieves the data and updates
 * the UI as required.
 */
final class MyDevicePresenter implements MyDeviceContract.Presenter {
    private static final String TAG = MyDevicePresenter.class.getName();

    private final MyDeviceContract.ViewModel mViewModel;
    private UserRepository mUserRepository;
    private CompositeDisposable mCompositeDisposable;

    MyDevicePresenter(MyDeviceContract.ViewModel viewModel, UserRepository userRepository) {
        mViewModel = viewModel;
        mUserRepository = userRepository;
        mCompositeDisposable = new CompositeDisposable();
        getCurrentUser();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mCompositeDisposable.clear();
    }

    @Override
    public void getCurrentUser() {
        Disposable disposable = mUserRepository.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<User>() {
                @Override
                public void accept(User user) throws Exception {
                    mViewModel.onGetUserSuccess(user);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetUserFailure(error.getMessage());
                }
            });

        mCompositeDisposable.add(disposable);
    }
}
