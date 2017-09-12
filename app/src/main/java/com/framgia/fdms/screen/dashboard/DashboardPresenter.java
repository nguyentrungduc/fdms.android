package com.framgia.fdms.screen.dashboard;

import com.framgia.fdms.data.model.User;
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

/**
 * Listens to user actions from the UI ({@link DashboardFragment}), retrieves the data and updates
 * the UI as required.
 */
public class DashboardPresenter implements DashboardContract.Presenter {
    private static final String TAG = DashboardPresenter.class.getName();
    private final DashboardContract.ViewModel mViewModel;
    private UserRepository mRepository;
    private CompositeDisposable mCompositeSubscription;
    private SharePreferenceApi mSharedPreferences;

    public DashboardPresenter(DashboardContract.ViewModel viewModel, UserRepository repository,
        SharePreferenceApi sharedPreferences) {
        mViewModel = viewModel;
        mRepository = repository;
        mCompositeSubscription = new CompositeDisposable();
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public void onStart() {
        getCurrentUser();
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

    @Override
    public void saveShowCase() {
        mSharedPreferences.put(IS_SHOW_CASE_MAIN, true);
    }
}
