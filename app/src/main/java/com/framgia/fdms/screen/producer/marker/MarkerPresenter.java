package com.framgia.fdms.screen.producer.marker;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.source.MarkerDataSource;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Listens to user actions from the UI ({@link MarkerFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
public class MarkerPresenter implements MarkerContract.Presenter {
    private final MarkerContract.ViewModel mViewModel;
    private MarkerDataSource mRepository;
    private CompositeDisposable mSubscription;
    private String mNameSearch;

    public MarkerPresenter(MarkerContract.ViewModel viewModel, MarkerDataSource repository) {
        mViewModel = viewModel;
        mRepository = repository;
        mSubscription = new CompositeDisposable();
        getMakers(FIRST_PAGE, PER_PAGE);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    @Override
    public void getMakers(int page, int perPage) {
        Disposable subscription = mRepository.getListMarker(mNameSearch, page, perPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<Producer>>() {
                @Override
                public void accept(List<Producer> producers) throws Exception {
                    mViewModel.onLoadMakerSucessfully(producers);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onLoadMakerFail();
                }
            });
        mSubscription.add(subscription);
    }
}
