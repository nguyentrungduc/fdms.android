package com.framgia.fdms.screen.device;

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
 * Listens to user actions from the UI ({@link DeviceFragment}), retrieves the data and updates
 * the UI as required.
 */
final class DevicePresenter implements DeviceContract.Presenter {
    private static final String TAG = DevicePresenter.class.getName();
    private final DeviceContract.ViewModel mViewModel;
    private UserRepository mUserRepository;
    private CompositeDisposable mCompositeSubscription;

    public DevicePresenter(DeviceContract.ViewModel viewModel, UserRepository userRepository) {
        mViewModel = viewModel;
        mUserRepository = userRepository;
        mCompositeSubscription = new CompositeDisposable();
        getCurrentUser();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mCompositeSubscription.clear();
    }

    @Override
    public void getCurrentUser() {
        Disposable subscription = mUserRepository.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<User>() {
                @Override
                public void accept(User user) throws Exception {
                    mViewModel.setupViewPager(user);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onError(error.getMessage());
                }
            });
        mCompositeSubscription.add(subscription);
    }
}
