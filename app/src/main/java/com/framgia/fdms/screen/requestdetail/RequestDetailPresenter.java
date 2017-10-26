package com.framgia.fdms.screen.requestdetail;

import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.source.RequestRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Listens to user actions from the UI ({@link RequestDetailActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
final class RequestDetailPresenter implements RequestDetailContract.Presenter {

    private final RequestDetailContract.ViewModel mViewModel;
    private RequestRepository mRequestRepository;
    private CompositeDisposable mCompositeDisposable;

    RequestDetailPresenter(RequestDetailContract.ViewModel viewModel,
                           RequestRepository requestRepository, int requestId) {
        mViewModel = viewModel;
        mRequestRepository = requestRepository;
        mCompositeDisposable = new CompositeDisposable();
        getRequest(requestId);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        mCompositeDisposable.clear();
    }

    @Override
    public void getRequest(int requestId) {
        Disposable disposable = mRequestRepository.getRequest(requestId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Request>() {
                    @Override
                    public void accept(Request request) throws Exception {
                        mViewModel.onGetRequestSuccess(request);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onGetRequestFailure(error.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });

        mCompositeDisposable.add(disposable);
    }
}
