package com.framgia.fdms.screen.producer.vendor;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.source.VendorDataSource;
import java.util.List;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Listens to user actions from the UI ({@link VendorFragment}), retrieves the data and updates
 * the UI as required.
 */
final class VendorPresenter implements VendorContract.Presenter {
    private final VendorContract.ViewModel mViewModel;
    private VendorDataSource.RemoteDataSource mRepository;
    private CompositeSubscription mSubscription;
    private int mPage;

    VendorPresenter(VendorContract.ViewModel viewModel,
            VendorDataSource.RemoteDataSource repository) {
        mViewModel = viewModel;
        mRepository = repository;
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void onStart() {
        getVendors(mPage);
    }

    @Override
    public void onStop() {
    }

    @Override
    public void getVendors(int page) {
        Subscription subscription = mRepository.getListVendor(page, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Producer>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onLoadVendorFailed();
                    }

                    @Override
                    public void onNext(List<Producer> vendors) {
                        mViewModel.onLoadVendorSuccess(vendors);
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
        Subscription subscription = mRepository.addVendor(producer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onActionError();
                    }

                    @Override
                    public void onNext(Void object) {
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void deleteVendor(Producer producer) {
        Subscription subscription = mRepository.deleteVendor(producer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onActionError();
                    }

                    @Override
                    public void onNext(Void object) {
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public void editVendor(Producer producer) {
        Subscription subscription = mRepository.editVendor(producer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onActionError();
                    }

                    @Override
                    public void onNext(Void object) {
                    }
                });
        mSubscription.add(subscription);
    }
}
