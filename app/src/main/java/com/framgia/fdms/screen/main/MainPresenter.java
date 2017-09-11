package com.framgia.fdms.screen.main;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.local.sharepref.SharePreferenceApi;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.framgia.fdms.data.source.local.sharepref.SharePreferenceKey.IS_SHOW_CASE_MAIN;
import static com.framgia.fdms.data.source.local.sharepref.SharePreferenceKey.IS_SHOW_CASE_REQUEST;

/**
 * Listens to user actions from the UI ({@link MainActivity}), retrieves the data and updates
 * the UI as required.
 */
public class MainPresenter implements MainContract.Presenter {
    private static final String TAG = MainPresenter.class.getName();
    private final MainContract.ViewModel mViewModel;
    private CompositeSubscription mSubscription;
    private DeviceRepository mDeviceRepository;
    private SharePreferenceApi mSharedPreferences;
    private UserRepository mUserRepository;

    public MainPresenter(MainContract.ViewModel viewModel, DeviceRepository deviceRepository,
                         SharePreferenceApi sharedPreferences, UserRepository userRepository) {
        mViewModel = viewModel;
        mSubscription = new CompositeSubscription();
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
        Subscription subscription = mDeviceRepository.getDeviceByQrCode(resultQrCode)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<Device>() {
                @Override
                public void call(Device device) {
                    mViewModel.onGetDecodeSuccess(device);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    mViewModel.onGetDeviceError(throwable.getMessage());
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void getCurrentUser() {
        Subscription subscription = mUserRepository.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<User>() {
                @Override
                public void call(User user) {
                    mViewModel.onGetUserSuccess(user);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    mViewModel.onError(throwable.getMessage());
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void logout() {
        mUserRepository.logout();
    }
}
