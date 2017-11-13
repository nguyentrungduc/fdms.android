package com.framgia.fdms.screen.request.userrequest;

import android.app.AlertDialog;

import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.RequestRepositoryContract;
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

import static com.framgia.fdms.screen.requestcreation.RequestCreatorType.MY_REQUEST;
import static com.framgia.fdms.utils.Constant.ALL_RELATIVE_ID;
import static com.framgia.fdms.utils.Constant.ALL_REQUEST_STATUS_ID;
import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Listens to user actions from the UI ({@link UserRequestFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
public final class UserRequestPresenter implements UserRequestContract.Presenter {

    private final UserRequestContract.ViewModel mViewModel;
    private int mPage = FIRST_PAGE;
    private CompositeDisposable mSubscription;
    private RequestRepositoryContract mRequestRepository;
    private UserRepository mUserRepository;
    private int mRelativeId = ALL_RELATIVE_ID;
    private int mStatusId = ALL_REQUEST_STATUS_ID;

    public UserRequestPresenter(UserRequestContract.ViewModel viewModel,
        RequestRepositoryContract deviceRepository, UserRepository userRepository) {
        mViewModel = viewModel;
        mSubscription = new CompositeDisposable();
        mRequestRepository = deviceRepository;
        mUserRepository = userRepository;
        getMyRequest(ALL_REQUEST_STATUS_ID, ALL_RELATIVE_ID, mPage, PER_PAGE);
        getCurrentUser();
    }

    @Override
    public void onLoadMore() {
        mPage++;
        getMyRequest(mStatusId, mRelativeId, mPage, PER_PAGE);
    }

    @Override
    public void getData(Status relative, Status status, Status assignee) {
        mPage = FIRST_PAGE;
        if (relative != null) {
            mRelativeId = relative.getId();
        }
        if (status != null) {
            mStatusId = status.getId();
        }

        getMyRequest(mStatusId, mRelativeId, mPage, PER_PAGE);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    @Override
    public void getMyRequest(int requestStatusId, int relativeId, final int page,
        final int perPage) {
        mViewModel.setRefresh(true);
        Disposable subscription =
            mRequestRepository.getRequests(MY_REQUEST, requestStatusId, relativeId, page, perPage)
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
                        mViewModel.setAllowLoadMore(requests != null && requests.size() == perPage);
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
                    mViewModel.onUpdateActionSuccess(requestRespone.getData());
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
    public void cancelRequest(final int requestId, int actionId, String description) {
        Disposable subscription = mRequestRepository.cancelRequest(requestId, actionId, description)
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
                    mViewModel.onUpdateActionSuccess(requestRespone.getData());
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
