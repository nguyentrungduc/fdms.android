package com.framgia.fdms.screen.producer;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.source.CategoryDataSource;
import com.framgia.fdms.data.source.DeviceGroupDataSource;
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

import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Listens to user actions from the UI ({@link ProducerFragment}), retrieves the data and updates
 * the UI as required.
 */
final class ProducerPresenter implements ProducerContract.Presenter {
    private final ProducerContract.ViewModel mViewModel;
    private VendorDataSource.RemoteDataSource mVendorRepository;
    private MarkerDataSource mMarkerRepository;
    private DeviceGroupDataSource mDeviceGroupRepository;
    private CategoryDataSource.RemoteDataSource mCategoryRepository;
    private CompositeDisposable mSubscription;
    private int mPage;
    @ProducerType
    private int mType;
    private String mName;
    private int mGroupTypeId;

    ProducerPresenter(ProducerContract.ViewModel viewModel, @ProducerType int type,
        VendorDataSource.RemoteDataSource vendorRepository, MarkerDataSource markerRepository,
        DeviceGroupDataSource deviceGroupRepository,
        CategoryDataSource.RemoteDataSource categoryRepository) {
        mViewModel = viewModel;
        mType = type;
        mVendorRepository = vendorRepository;
        mMarkerRepository = markerRepository;
        mDeviceGroupRepository = deviceGroupRepository;
        mCategoryRepository = categoryRepository;
        mSubscription = new CompositeDisposable();
        mPage++;
        getVendors();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    @Override
    public void getVendors() {
        Observable<List<Producer>> observable;
        switch (mType) {
            case ProducerType.VENDOR:
                observable = mVendorRepository.getListVendor(mName, mPage, PER_PAGE);
                break;
            case ProducerType.DEVICE_GROUPS:
                observable = mDeviceGroupRepository.getListDeviceGroup(mName);
                break;
            case ProducerType.CATEGORIES_GROUPS:
                observable = mCategoryRepository.getListCategory();
                mViewModel.setShowCategoryFilter(true);
                break;
            default:
            case ProducerType.MARKER:
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
                    mViewModel.setAllowLoadMore(producers != null && producers.size() == PER_PAGE);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onLoadVendorFailed(error.getMessage());
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
        if (mType == ProducerType.DEVICE_GROUPS) {
            mViewModel.hideProgress();
            return;
        }
        mPage++;
        getVendors();
    }

    @Override
    public void addVendor(Producer producer) {
        Observable<Producer> observable;
        switch (mType) {
            case ProducerType.VENDOR:
                observable = mVendorRepository.addVendor(producer);
                break;
            case ProducerType.DEVICE_GROUPS:
                observable = mDeviceGroupRepository.addDeviceGroup(producer);
                break;
            default:
            case ProducerType.MARKER:
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
            case ProducerType.VENDOR:
                observable = mVendorRepository.deleteVendor(producer);
                break;
            case ProducerType.DEVICE_GROUPS:
                observable = mDeviceGroupRepository.deleteDeviceGroup(producer);
                break;
            default:
            case ProducerType.MARKER:
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
            case ProducerType.VENDOR:
                observable = mVendorRepository.editVendor(producer);
                break;
            case ProducerType.DEVICE_GROUPS:
                observable = mDeviceGroupRepository.editDeviceGroup(producer);
                break;
            default:
            case ProducerType.MARKER:
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
