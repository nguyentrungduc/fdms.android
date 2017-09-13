package com.framgia.fdms.screen.deviceselection;

import android.text.TextUtils;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.source.DeviceRepository;
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
 * Listens to user actions from the UI ({@link DeviceSelectionActivity}), retrieves the data and
 * updates the UI as required.
 */
public class DeviceSelectionPresenter implements DeviceSelectionContract.Presenter {
    private static final String TAG = DeviceSelectionPresenter.class.getName();
    private final DeviceSelectionContract.ViewModel mViewModel;
    private CompositeDisposable mCompositeSubscriptions = new CompositeDisposable();
    private int mPage = FIRST_PAGE;
    private String mKeyWord = NOT_SEARCH;
    private int mCategoryId = OUT_OF_INDEX;
    private int mStatusId = OUT_OF_INDEX;
    private DeviceRepository mDeviceRepository;

    public DeviceSelectionPresenter(DeviceSelectionContract.ViewModel viewModel,
        DeviceRepository deviceRepository, int categoryId) {
        mViewModel = viewModel;
        mDeviceRepository = deviceRepository;
        mCategoryId = categoryId;
        getData(null);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mCompositeSubscriptions.clear();
    }

    @Override
    public void getListDevice(String deviceName, int categoryId, int statusId, int page,
        int perPage) {
        Disposable subscription =
            mDeviceRepository.getListDevices(deviceName, categoryId, statusId, page, perPage)
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
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onError(error.getMessage());
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
    public void getData(String keyWord) {
        mKeyWord = TextUtils.isEmpty(keyWord) ? NOT_SEARCH : keyWord;
        mPage = FIRST_PAGE;
        getListDevice(mKeyWord, mCategoryId, mStatusId, mPage, PER_PAGE);
    }

    @Override
    public void loadMoreData() {
        mPage++;
        getListDevice(mKeyWord, mCategoryId, mStatusId, mPage, PER_PAGE);
    }
}
