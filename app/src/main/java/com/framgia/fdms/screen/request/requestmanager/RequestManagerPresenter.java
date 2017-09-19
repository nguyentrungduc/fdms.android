package com.framgia.fdms.screen.request.requestmanager;

import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.RequestRepositoryContract;
import com.framgia.fdms.data.source.StatusRepository;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

import static com.framgia.fdms.screen.request.RequestPagerAdapter.RequestPage.MANAGER_REQUEST;
import static com.framgia.fdms.utils.Constant.ALL_RELATIVE_ID;
import static com.framgia.fdms.utils.Constant.ALL_REQUEST_STATUS_ID;
import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Listens to user actions from the UI ({@link RequestManagerFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
public final class RequestManagerPresenter implements RequestManagerContract.Presenter {
    private final RequestManagerContract.ViewModel mViewModel;
    private int mPage = FIRST_PAGE;
    private CompositeDisposable mSubscription;
    private RequestRepositoryContract mRequestRepository;
    private UserRepository mUserRepository;
    private int mRelativeId = ALL_RELATIVE_ID;
    private int mStatusId = ALL_REQUEST_STATUS_ID;

    public RequestManagerPresenter(RequestManagerContract.ViewModel viewModel,
        RequestRepositoryContract deviceRepository,
        UserRepository userRepository) {
        mViewModel = viewModel;
        mSubscription = new CompositeDisposable();
        mRequestRepository = deviceRepository;
        mUserRepository = userRepository;
        getRequest(ALL_REQUEST_STATUS_ID, ALL_RELATIVE_ID, mPage, PER_PAGE);
        getCurrentUser();
    }

    @Override
    public void onLoadMore() {
        mPage++;
        getRequest(mStatusId, mRelativeId, mPage, PER_PAGE);
    }

    @Override
    public void getData(Status relative, Status status) {
        mViewModel.setRefresh(true);
        mPage = FIRST_PAGE;
        if (relative != null) {
            mRelativeId = relative.getId();
        }
        if (status != null) {
            mStatusId = status.getId();
        }
        getRequest(mStatusId, mRelativeId, mPage, PER_PAGE);
    }

    @Override
    public void updateActionRequest(int requestId, int actionId) {
        Disposable subscription = mRequestRepository.updateActionRequest(requestId, actionId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgressbar();
                }
            })
            .subscribe(new Consumer<Respone<Request>>() {
                @Override
                public void accept(Respone<Request> requestRespone) throws Exception {
                    mViewModel.onUpdateActionSuccess(requestRespone);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.hideProgressbar();
                    mViewModel.onLoadError(error.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgressbar();
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    @Override
    public void getRequest(int requestStatusId, int relativeId, int perPage, int page) {
        Disposable subscription =
            mRequestRepository.getRequests(MANAGER_REQUEST, requestStatusId, relativeId, page,
                perPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mViewModel.showProgressbar();
                    }
                })
                .subscribe(new Consumer<List<Request>>() {
                    @Override
                    public void accept(List<Request> requests) throws Exception {
                        mViewModel.onGetRequestSuccess(requests);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.hideProgressbar();
                        mViewModel.setRefresh(false);
                        mViewModel.onGetRequestError();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mViewModel.hideProgressbar();
                        mViewModel.setRefresh(false);
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
                    mViewModel.setCurrentUser(user);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onLoadError(error.getMessage());
                }
            });
        mSubscription.add(subscription);
    }
}
