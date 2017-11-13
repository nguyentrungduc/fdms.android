package com.framgia.fdms.screen.deviceselection;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.CategoryRepository;
import com.framgia.fdms.data.source.DeviceGroupRepository;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import com.framgia.fdms.data.source.remote.CategoryRemoteDataSource;
import com.framgia.fdms.screen.device.listdevice.DeviceFilterModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

import static com.framgia.fdms.utils.Constant.AVAIABLE;
import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Listens to user actions from the UI ({@link DeviceSelectionActivity}), retrieves the data and
 * updates the UI as required.
 */
public class DeviceSelectionPresenter implements DeviceSelectionContract.Presenter {

    private final DeviceSelectionContract.ViewModel mViewModel;
    private CompositeDisposable mCompositeSubscriptions = new CompositeDisposable();
    private int mPage = FIRST_PAGE;
    private int mCategoryPage = FIRST_PAGE;
    private DeviceRepository mDeviceRepository;
    private DeviceFilterModel mFilterModel;

    private DeviceGroupRepository mGroupRepository;
    private CategoryRepository mCategoryRepository;

    public DeviceSelectionPresenter(DeviceSelectionContract.ViewModel viewModel,
                                    DeviceRepository deviceRepository, int categoryId) {
        mViewModel = viewModel;
        mDeviceRepository = deviceRepository;

        mFilterModel = new DeviceFilterModel();

        mFilterModel.setCategory(new Status(OUT_OF_INDEX));
        mFilterModel.setStatus(new Status(AVAIABLE));

        mGroupRepository = DeviceGroupRepository.getInstance();
        mCategoryRepository = CategoryRepository.getInstance(CategoryRemoteDataSource.getInstance());
        getData(null, -1);
        getGroup();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mCompositeSubscriptions.clear();
    }

    @Override
    public void getListDevice(DeviceFilterModel filterModel, int page) {
        Disposable subscription = mDeviceRepository.getListDevices(filterModel, page, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mViewModel.showProgressbar();
                    }
                })
                .subscribe(new Consumer<List<Device>>() {
                    @Override
                    public void accept(List<Device> devices) throws Exception {
                        mViewModel.onGetDeviceSucces(devices);
                        mViewModel.setAllowLoadMore(devices.size() >= PER_PAGE);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onError(error.getMessage());
                        mViewModel.setAllowLoadMore(true);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mViewModel.hideProgressbar();
                    }
                });
        mCompositeSubscriptions.add(subscription);
    }

    @Override
    public void getData(String keyWord, int categoryId) {
        mPage = FIRST_PAGE;
        mFilterModel.setDeviceName(keyWord);
        mFilterModel.getCategory().setId(categoryId);
        getListDevice(mFilterModel, mPage);
    }

    @Override
    public void loadMoreData() {
        mPage++;
        getListDevice(mFilterModel, mPage);
    }

    @Override
    public void getCategory(final int id, int page) {
        mCategoryPage = page;
        mCategoryRepository.getListCategory("", id, mCategoryPage, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Producer>>() {
                    @Override
                    public void accept(List<Producer> producers) throws Exception {
                        mViewModel.updateDefaultAdapter();
                        mViewModel.updateCategoryData(producers);
                        if (producers.size() == PER_PAGE) {
                            mCategoryPage++;
                            getCategory(id, mCategoryPage);
                        }
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onGetCategoryError(error.getMessage());
                    }
                });
    }

    @Override
    public void getGroup() {
        Disposable disposable = mGroupRepository.getListDeviceGroup("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Producer>>() {
                    @Override
                    public void accept(List<Producer> producers) throws Exception {
                        mViewModel.updateDefaultAdapter();
                        mViewModel.updateGroupData(producers);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onGetGroupError(error.getMessage());
                    }
                });

        mCompositeSubscriptions.add(disposable);
    }

}
