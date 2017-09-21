package com.framgia.fdms.screen.profile;

import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import com.framgia.fdms.data.source.local.sharepref.SharePreferenceApi;
import com.google.gson.Gson;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.framgia.fdms.data.source.local.sharepref.SharePreferenceKey.USER_PREFS;

/**
 * Listens to user actions from the UI ({@link ProfileFragment}), retrieves the data and updates
 * the UI as required.
 */
final class ProfilePresenter implements ProfileContract.Presenter {
    private static final String TAG = ProfilePresenter.class.getName();

    private final ProfileContract.ViewModel mViewModel;
    private UserRepository mRepository;
    private CompositeDisposable mCompositeSubscription;
    private SharePreferenceApi mSharedPreferences;

    ProfilePresenter(ProfileContract.ViewModel viewModel, UserRepository repository,
        SharePreferenceApi sharedPreferences) {
        mViewModel = viewModel;
        mRepository = repository;
        mSharedPreferences = sharedPreferences;
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
        Disposable subscription = mRepository.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<User>() {
                @Override
                public void accept(User user) throws Exception {
                    mViewModel.setCurrentUser(user);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onError(error.getMessage());
                }
            });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void updateUserProfile(int userId, String gender, String address, String birthday) {
        Disposable subscription = mRepository.updateUserProfile(userId, gender, address, birthday)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Respone<User>>() {
                @Override
                public void accept(@NonNull Respone<User> userRespone) throws Exception {
                    mViewModel.onUpdateProfileSuccess(userRespone.getData());
                    saveUser(userRespone.getData());
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onError(error.getMessage());
                }
            });
        mCompositeSubscription.add(subscription);
    }

    private void saveUser(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        mSharedPreferences.put(USER_PREFS, json);
    }
}
