package com.framgia.fdms.screen.producer.vendor;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.source.VendorDataSource;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Listens to user actions from the UI ({@link VendorFragment}), retrieves the data and updates
 * the UI as required.
 */
final class VendorPresenter implements VendorContract.Presenter {
    private final VendorContract.ViewModel mViewModel;
    private VendorDataSource.RemoteDataSource mRepository;
    private CompositeDisposable mSubscription;
    private int mPage;

    VendorPresenter(VendorContract.ViewModel viewModel,
        VendorDataSource.RemoteDataSource repository) {
        mViewModel = viewModel;
        mRepository = repository;
        mSubscription = new CompositeDisposable();
    }

    @Override
    public void onStart() {
        mPage++;
        getVendors(mPage);
    }

    @Override
    public void onStop() {
    }

    @Override
    public void getVendors(int page) {
        Disposable subscription = mRepository.getListVendor(page, PER_PAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<Producer>>() {
                @Override
                public void accept(List<Producer> producers) throws Exception {
                    if (producers == null || producers.size() == 0) {
                        mPage--;
                    }
                    mViewModel.onLoadVendorSuccess(producers);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onLoadVendorFailed();
                    mPage--;
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void loadMorePage() {
        mPage++;
        getVendors(mPage);
    }

    @Override
    public void addVendor(Producer producer) {
        Disposable subscription = mRepository.addVendor(producer)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Producer>() {
                @Override
                public void accept(Producer producer) throws Exception {
                    mViewModel.onAddVendorSuccess(producer);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onAddVendorFailed(error.getMessage());
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void deleteVendor(final Producer producer) {
        Disposable subscription = mRepository.deleteVendor(producer)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Respone<String>>() {
                @Override
                public void accept(Respone<String> respone) throws Exception {
                    if (!respone.isError()) {
                        mViewModel.onDeleteVendorSuccess(producer);
                    }
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onDeleteVendorFailed(error.getMessage());
                }
            });

        mSubscription.add(subscription);
    }

    @Override
    public void editVendor(final Producer producer) {
        Disposable subscription = mRepository.editVendor(producer)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<String>() {
                @Override
                public void accept(String respone) throws Exception {
                    mViewModel.onUpdateVendorSuccess(producer, respone);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onUpdateVendorFailed(error.getMessage());
                }
            });
        mSubscription.add(subscription);
    }
}
