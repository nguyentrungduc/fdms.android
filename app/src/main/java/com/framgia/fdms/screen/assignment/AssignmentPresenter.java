package com.framgia.fdms.screen.assignment;

import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.RequestRepository;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Listens to user actions from the UI ({@link AssignmentActivity}), retrieves the data and updates
 * the UI as required.
 */
final class AssignmentPresenter implements AssignmentContract.Presenter {
    private static final String TAG = AssignmentPresenter.class.getName();

    private final AssignmentContract.ViewModel mViewModel;
    private int mRequestId;
    private RequestRepository mRequestRepository;
    private UserRepository mUserRepository;
    private CompositeDisposable mSubscription;

    public AssignmentPresenter(AssignmentContract.ViewModel viewModel, int requestId,
        RequestRepository requestRepository, UserRepository userRepository) {
        mViewModel = viewModel;
        mRequestId = requestId;
        mRequestRepository = requestRepository;
        mUserRepository = userRepository;
        mSubscription = new CompositeDisposable();
        getRequest(mRequestId);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    @Override
    public void registerAssignment(Request request) {
        Disposable subscription = mRequestRepository.getRequest(mRequestId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Consumer<Request>() {
                @Override
                public void accept(Request request) throws Exception {
                    mViewModel.onGetRequestSuccess(request);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onLoadError(error.getMessage());
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void getRequest(int requestId) {
        Disposable subscription = mRequestRepository.getRequest(requestId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Consumer<Request>() {
                @Override
                public void accept(Request request) throws Exception {
                    mViewModel.onGetRequestSuccess(request);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onLoadError(error.getMessage());
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void chooseExportActivity() {
        Disposable subscription = mUserRepository.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<User>() {
                @Override
                public void accept(User user) throws Exception {
                    mViewModel.openChooseExportActivitySuccess(user);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onChooseExportActivityFailed();
                }
            });
        mSubscription.add(subscription);
    }
}
