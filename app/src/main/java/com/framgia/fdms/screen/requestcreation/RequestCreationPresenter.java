package com.framgia.fdms.screen.requestcreation;

import android.text.TextUtils;

import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.RequestRepository;
import com.framgia.fdms.data.source.StatusRepository;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import com.framgia.fdms.data.source.api.request.RequestCreatorRequest;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.framgia.fdms.data.anotation.Permission.BO_MANAGER;
import static com.framgia.fdms.utils.Constant.PER_PAGE;


/**
 * Listens to user actions from the UI ({@link RequestCreationActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
public final class RequestCreationPresenter implements RequestCreationContract.Presenter {
    private final RequestCreationContract.ViewModel mViewModel;
    private CompositeDisposable mSubscription;
    private RequestRepository mRequestRepository;
    private UserRepository mUserRepository;
    private StatusRepository mStatusRepository;
    private User mUser;
    @RequestCreatorType
    private int mRequestType;

    public RequestCreationPresenter(RequestCreationContract.ViewModel viewModel,
                                    @RequestCreatorType int requestType,
                                    RequestRepository requestRepository,
                                    UserRepository userRepository,
                                    StatusRepository statusRepository) {
        mViewModel = viewModel;
        mRequestType = requestType;
        mSubscription = new CompositeDisposable();
        mRequestRepository = requestRepository;
        mUserRepository = userRepository;
        mStatusRepository = statusRepository;
        getDefaultAssignee();
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
                        mUser = user;
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
    public void registerRequest(RequestCreatorRequest request) {
        if (!validateDataInput(request)) return;
        Disposable subscription = mRequestRepository.registerRequest(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mViewModel.showProgressbar();
                    }
                })
                .subscribe(new Consumer<Request>() {
                    @Override
                    public void accept(Request request) throws Exception {
                        mViewModel.onGetRequestSuccess(request);
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
    public boolean validateDataInput(RequestCreatorRequest request) {
        boolean isValid = true;
        if (TextUtils.isEmpty(request.getTitle())) {
            isValid = false;
            mViewModel.onInputTitleError();
        }
        if (mUser.getRole().equals(BO_MANAGER) &&
                mRequestType == RequestCreatorType.MEMBER_REQUEST &&
                request.getRequestFor() <= 0) {
            isValid = false;
            mViewModel.onInputRequestForError();
        }
        return isValid;
    }

    @Override
    public void getDefaultAssignee() {
        Disposable disposable = mStatusRepository.getListAssignee()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Status>>() {
                    @Override
                    public void accept(@NonNull List<Status> assignees) throws Exception {
                        if (assignees != null && assignees.size() != 0) {
                            mViewModel.onGetDefaultAssignSuccess(assignees.get(0));
                        }
                    }
                });
        mSubscription.add(disposable);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }
}
