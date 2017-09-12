package com.framgia.fdms.screen.device.listdevice;

import com.framgia.fdms.data.model.Category;
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
    private int mPage = FIRST_PAGE;
    private DeviceRepository mDeviceRepository;
    private DeviceReturnRepository mReturnRepository;
    private CategoryRepository mCategoryRepository;
    private StatusRepository mStatusRepository;
    private String mKeyWord = NOT_SEARCH;
    private int mCategoryId = OUT_OF_INDEX;
    private int mStatusId = OUT_OF_INDEX;
    private UserRepository mUserRepository;

    public ListDevicePresenter(ListDeviceContract.ViewModel viewModel,
        DeviceRepository deviceRepository, CategoryRepository categoryRepository,
        StatusRepository statusRepository, UserRepository userRepository,
        DeviceReturnRepository returnRepository) {
        mViewModel = viewModel;
        mDeviceRepository = deviceRepository;
        mCategoryRepository = categoryRepository;
        mStatusRepository = statusRepository;
        mUserRepository = userRepository;
        mReturnRepository = returnRepository;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mCompositeSubscriptions.clear();
    }

    @Override
    public void getDevicesBorrow() {
        Disposable subscription = mReturnRepository.devicesOfBorrower()
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
                    mViewModel.onDeviceLoaded(devices);
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
                        mViewModel.onDeviceLoaded(devices);
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

    public void getListCategories() {
        Disposable subscription = mCategoryRepository.getListCategory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgressbar();
                }
            })
            .subscribe(new Consumer<List<Category>>() {
                @Override
                public void accept(List<Category> categories) throws Exception {
                    mViewModel.onDeviceCategoryLoaded(categories);
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

    public void getListStatuses() {
        Disposable subscription = mStatusRepository.getListStatus()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgressbar();
                }
            })
            .subscribe(new Consumer<List<Status>>() {
                @Override
                public void accept(List<Status> statuses) throws Exception {
                    mViewModel.onDeviceStatusLoaded(statuses);
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
    public void loadMoreData() {
        mPage++;
        getListDevice(mKeyWord, mCategoryId, mStatusId, mPage, PER_PAGE);
    }

    @Override
    public void getData(String keyWord, Category category, Status status) {
        mPage = FIRST_PAGE;
        if (category != null) {
            mCategoryId = category.getId();
        }
        if (status != null) {
            mStatusId = status.getId();
        }
        if (keyWord != null) {
            mKeyWord = keyWord;
        }
        getListDevice(mKeyWord, mCategoryId, mStatusId, mPage, PER_PAGE);
        getListCategories();
        getListStatuses();
        getCurrentUser();
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
