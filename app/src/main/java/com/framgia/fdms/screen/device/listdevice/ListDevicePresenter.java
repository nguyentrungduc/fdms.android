package com.framgia.fdms.screen.device.listdevice;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.CategoryRepository;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.DeviceReturnRepository;
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

import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.NOT_SEARCH;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Listens to user actions from the UI ({@link ListDeviceFragment}), retrieves the data and updates
 * the UI as required.
 */
final class ListDevicePresenter implements ListDeviceContract.Presenter {
    private final ListDeviceContract.ViewModel mViewModel;
    private CompositeDisposable mCompositeSubscriptions = new CompositeDisposable();
    private DeviceRepository mDeviceRepository;
    private DeviceReturnRepository mReturnRepository;
    private UserRepository mUserRepository;

    private int mPage = FIRST_PAGE;
    private DeviceFilterModel mFilterModel;

    public ListDevicePresenter(ListDeviceContract.ViewModel viewModel,
        DeviceRepository deviceRepository, DeviceReturnRepository returnRepository,
        UserRepository userRepository) {
        mViewModel = viewModel;
        mDeviceRepository = deviceRepository;
        mUserRepository = userRepository;
        mReturnRepository = returnRepository;
    }

    @Override
    public void onStart() {
        getCurrentUser();
    }

    @Override
    public void onStop() {
        mCompositeSubscriptions.clear();
    }

    @Override
    public void loadMoreData() {
        mPage++;
        getData(mFilterModel, mPage);
    }

    @Override
    public void getData(DeviceFilterModel filterModel, int page) {
        if (filterModel == null) {
            return;
        }
        mFilterModel = filterModel;
        Disposable disposable = mDeviceRepository.getListDevices(mFilterModel, page, PER_PAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgressbar();
                    mViewModel.onStartGetData();
                }
            })
            .subscribe(new Consumer<List<Device>>() {
                @Override
                public void accept(List<Device> devices) throws Exception {
                    mViewModel.onDeviceLoaded(devices);
                    mViewModel.setAllowLoadMore(devices != null && devices.size() == PER_PAGE);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onError(error.getMessage());
                    mViewModel.hideProgressbar();
                    mPage--;
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgressbar();
                }
            });
        mCompositeSubscriptions.add(disposable);
    }

    @Override
    public void getCurrentUser() {
        Disposable subscription = mUserRepository.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<User>() {
                @Override
                public void accept(User user) throws Exception {
                    mViewModel.setupFloatingActionsMenu(user);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onError(error.getMessage());
                }
            });
        mCompositeSubscriptions.add(subscription);
    }
}
