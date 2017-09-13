package com.framgia.fdms.screen.producer.vendor;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.source.MarkerDataSource;
import com.framgia.fdms.data.source.VendorDataSource;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

import static com.framgia.fdms.screen.producer.vendor.VendorFragment.ProductType;
import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Listens to user actions from the UI ({@link VendorFragment}), retrieves the data and updates
 * the UI as required.
 */
final class VendorPresenter implements VendorContract.Presenter {
    private final VendorContract.ViewModel mViewModel;
    private VendorDataSource.RemoteDataSource mVendorRepository;
    private MarkerDataSource mMarkerRepository;
    private CompositeDisposable mSubscription;
    private int mPage;
    @ProductType
    private int mType;
    private String mName;

    VendorPresenter(VendorContract.ViewModel viewModel, @ProductType int type,
        VendorDataSource.RemoteDataSource vendorRepository, MarkerDataSource markerRepository) {
        mViewModel = viewModel;
        mType = type;
        mVendorRepository = vendorRepository;
        mMarkerRepository = markerRepository;
        mSubscription = new CompositeDisposable();
    }

    @Override
    public void onStart() {
        mPage++;
        getVendors();
    }

    @Override
    public void onStop() {
    }

    @Override
    public void getVendors() {
        Observable<List<Producer>> observable;
        switch (mType) {
            case ProductType.VENDOR:
                observable = mVendorRepository.getListVendor(mName, mPage, PER_PAGE);
                break;

            default:
            case ProductType.MARKER:
                observable = mMarkerRepository.getListMarker(mName, mPage, PER_PAGE);
                break;
        }

        Disposable subscription = observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgress();
                }
            })
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
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgress();
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void loadMorePage() {
        mPage++;
        getVendors();
    }

    @Override
    public void addVendor(Producer producer) {
        Observable<Producer> observable;
        switch (mType) {
            case ProductType.VENDOR:
                observable = mVendorRepository.addVendor(producer);
                break;

            default:
            case ProductType.MARKER:
                observable = mMarkerRepository.addMarker(producer);
                break;
        }

        Disposable subscription = observable.subscribeOn(Schedulers.io())
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
        Observable<Respone<String>> observable;
        switch (mType) {
            case ProductType.VENDOR:
                observable = mVendorRepository.deleteVendor(producer);
                break;

            default:
            case ProductType.MARKER:
                observable = mMarkerRepository.deleteMarker(producer);
                break;
        }

        Disposable subscription = observable.subscribeOn(Schedulers.io())
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
        Observable<String> observable;
        switch (mType) {
            case ProductType.VENDOR:
                observable = mVendorRepository.editVendor(producer);
                break;

            default:
            case ProductType.MARKER:
                observable = mMarkerRepository.editMarker(producer);
                break;
        }
        Disposable subscription = observable.subscribeOn(Schedulers.io())
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

    @Override
    public void getVendors(String name) {
        mName = name;
        mPage = FIRST_PAGE;
        getVendors();
    }
}
