package com.framgia.fdms.screen.main;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import com.framgia.fdms.data.source.local.sharepref.SharePreferenceApi;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.framgia.fdms.data.source.local.sharepref.SharePreferenceKey.IS_SHOW_CASE_MAIN;
import static com.framgia.fdms.data.source.local.sharepref.SharePreferenceKey.IS_SHOW_CASE_REQUEST;

/**
 * Listens to user actions from the UI ({@link MainActivity}), retrieves the data and updates
 * the UI as required.
 */
public class MainPresenter implements MainContract.Presenter {
    private static final String TAG = MainPresenter.class.getName();
    private final MainContract.ViewModel mViewModel;
    private CompositeDisposable mSubscription;
    private DeviceRepository mDeviceRepository;
    private SharePreferenceApi mSharedPreferences;
    private UserRepository mUserRepository;

    public MainPresenter(MainContract.ViewModel viewModel, DeviceRepository deviceRepository,
        SharePreferenceApi sharedPreferences, UserRepository userRepository) {
        mViewModel = viewModel;
        mSubscription = new CompositeDisposable();
        mDeviceRepository = deviceRepository;
        mSharedPreferences = sharedPreferences;
        mUserRepository = userRepository;
        mViewModel.setShowCase(mSharedPreferences.get(IS_SHOW_CASE_MAIN, Boolean.class));
        mViewModel.setShowCaseRequest(mSharedPreferences.get(IS_SHOW_CASE_REQUEST, Boolean.class));
        getCurrentUser();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    @Override
    public void getDevice(String resultQrCode) {
        Disposable subscription = mDeviceRepository.getDeviceByQrCode(resultQrCode)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Consumer<Device>() {
                @Override
                public void accept(Device device) throws Exception {
                    mViewModel.onGetDecodeSuccess(device);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDeviceError(error.getMessage());
                }
            });

        mSubscription.add(subscription);
    }

    @Override
    public void getCurrentUser() {
        Disposable subscription = mUserRepository.getCurrentUser()
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
                    mViewModel.onError(error.getMessage());
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void logout() {
        mUserRepository.logout();
    }
}
