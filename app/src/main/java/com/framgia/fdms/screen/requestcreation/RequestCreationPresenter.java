package com.framgia.fdms.screen.requestcreation;

import android.text.TextUtils;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.source.RequestRepository;
import com.framgia.fdms.data.source.api.request.RequestCreatorRequest;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Listens to user actions from the UI ({@link RequestCreationActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
public final class RequestCreationPresenter implements RequestCreationContract.Presenter {
    private final RequestCreationContract.ViewModel mViewModel;
    private CompositeSubscription mSubscription;
    private RequestRepository mRequestRepository;

    public RequestCreationPresenter(RequestCreationContract.ViewModel viewModel,
            RequestRepository requestRepository) {
        mViewModel = viewModel;
        mSubscription = new CompositeSubscription();
        mRequestRepository = requestRepository;
    }

    @Override
    public void registerRequest(RequestCreatorRequest request) {
        if (!validateDataInput(request)) return;
        Subscription subscription = mRequestRepository.registerRequest(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mViewModel.showProgressbar();
                    }
                })
                .subscribe(new Subscriber<Request>() {
                    @Override
                    public void onCompleted() {
                        mViewModel.hideProgressbar();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.hideProgressbar();
                        mViewModel.onLoadError(e.getMessage());
                    }

                    @Override
                    public void onNext(Request request) {
                        mViewModel.onGetRequestSuccess(request);
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
        if (TextUtils.isEmpty(request.getDescription())) {
            isValid = false;
            mViewModel.onInputDescriptionError();
        }

        return isValid;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }
}
