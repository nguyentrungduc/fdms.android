package com.framgia.fdms.screen.selection;

import android.text.TextUtils;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.BranchRepository;
import com.framgia.fdms.data.source.CategoryRepository;
import com.framgia.fdms.data.source.DeviceGroupRepository;
import com.framgia.fdms.data.source.DeviceUsingHistoryRepository;
import com.framgia.fdms.data.source.MarkerRepository;
import com.framgia.fdms.data.source.MeetingRoomRepository;
import com.framgia.fdms.data.source.StatusRepository;
import com.framgia.fdms.data.source.VendorRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

import static com.framgia.fdms.screen.selection.SelectionType.ASSIGNEE;
import static com.framgia.fdms.screen.selection.SelectionType.BRANCH;
import static com.framgia.fdms.screen.selection.SelectionType.CATEGORY;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_GROUP;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_USING_HISTORY;
import static com.framgia.fdms.screen.selection.SelectionType.MARKER;
import static com.framgia.fdms.screen.selection.SelectionType.MEETING_ROOM;
import static com.framgia.fdms.screen.selection.SelectionType.RELATIVE_STAFF;
import static com.framgia.fdms.screen.selection.SelectionType.STATUS;
import static com.framgia.fdms.screen.selection.SelectionType.STATUS_REQUEST;
import static com.framgia.fdms.screen.selection.SelectionType.USER_BORROW;
import static com.framgia.fdms.screen.selection.SelectionType.VENDOR;
import static com.framgia.fdms.utils.Constant.NONE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.PER_PAGE;
import static com.framgia.fdms.utils.Constant.TITLE_ALL;
import static com.framgia.fdms.utils.Constant.TITLE_NA;

/**
 * Listens to user actions from the UI ({@link SelectionActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
public final class SelectionPresenter implements SelectionContract.Presenter {

    private final SelectionContract.ViewModel mViewModel;
    @SelectionType
    private int mSelectionType;
    private CompositeDisposable mCompositeDisposable;
    private StatusRepository mStatusRepository;
    private CategoryRepository mCategoryRepository;
    private VendorRepository mVendorRepository;
    private MarkerRepository mMarkerRepository;
    private BranchRepository mBranchRepository;
    private MeetingRoomRepository mMeetingRoomRepository;
    private int mPage = 1;
    private String mKeySearch;
    private int mDeviceGroupId;
    private DeviceGroupRepository mDeviceGroupRepository;
    private DeviceUsingHistoryRepository mDeviceUsingHistoryRepository;

    public SelectionPresenter(SelectionContract.ViewModel viewModel,
        @SelectionType int selectionType) {
        mViewModel = viewModel;
        mSelectionType = selectionType;
        mCompositeDisposable = new CompositeDisposable();
    }

    public void setStatusRepository(StatusRepository statusRepository) {
        mStatusRepository = statusRepository;
    }

    public void setCategoryRepository(CategoryRepository categoryRepository) {
        mCategoryRepository = categoryRepository;
    }

    public void setVendorRepository(VendorRepository vendorRepository) {
        mVendorRepository = vendorRepository;
    }

    public void setMarkerRepository(MarkerRepository markerRepository) {
        mMarkerRepository = markerRepository;
    }

    public void setBranchRepository(BranchRepository branchRepository) {
        mBranchRepository = branchRepository;
    }

    public void setMeetingRoomRepository(MeetingRoomRepository meetingRoomRepository) {
        mMeetingRoomRepository = meetingRoomRepository;
    }

    public void setDeviceGroupRepository(DeviceGroupRepository deviceGroupRepository) {
        mDeviceGroupRepository = deviceGroupRepository;
    }

    public void setDeviceGroupId(int deviceGroupId) {
        mDeviceGroupId = deviceGroupId;
    }

    public void setDeviceUsingHistoryRepository(
        DeviceUsingHistoryRepository deviceUsingHistoryRepository) {
        mDeviceUsingHistoryRepository = deviceUsingHistoryRepository;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mCompositeDisposable.clear();
    }

    @Override
    public void getData(String query) {
        mKeySearch = query;
        switch (mSelectionType) {
            case STATUS:
                getListStatus();
                break;
            case CATEGORY:
                getListCategory();
                break;
            case VENDOR:
                getListVendor();
                break;
            case MARKER:
                getListMarker();
                break;
            case BRANCH:
                getListBranch();
                break;
            case MEETING_ROOM:
                getListMeetingRoom();
                break;
            case DEVICE_GROUP:
                getDeviceGroups();
                break;
            case DEVICE_USING_HISTORY:
                getDeviceUsingHistoryStatus();
                break;
            case STATUS_REQUEST:
                getListStatusRequest();
                break;
            case RELATIVE_STAFF:
                getListRelative();
                break;
            case ASSIGNEE:
                getListAssignee();
                break;
            case USER_BORROW:
                getListUserBorrow();
                break;
            default:
                break;
        }
    }

    @Override
    public void getListRelative() {
        Disposable subscription = mStatusRepository.getListRelative(mKeySearch)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<Status>>() {
                @Override
                public void accept(List<Status> statuses) throws Exception {
                    if (TextUtils.isEmpty(mKeySearch) && statuses != null && statuses.size() != 0) {
                        statuses.add(0, new Status(OUT_OF_INDEX, TITLE_NA));
                    }
                    mViewModel.onGetDataSuccess(statuses);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDataFailed(error.getMessage());
                    mViewModel.hideProgress();
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgress();
                }
            });
        mCompositeDisposable.add(subscription);
    }

    @Override
    public void getListStatusRequest() {
        Disposable disposable = mStatusRepository.getListStatusRequest(mKeySearch)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgress();
                }
            })
            .subscribe(new Consumer<List<Status>>() {
                @Override
                public void accept(List<Status> statuses) throws Exception {
                    if (TextUtils.isEmpty(mKeySearch) && statuses != null && statuses.size() != 0) {
                        statuses.add(0, new Status(OUT_OF_INDEX, TITLE_NA));
                    }
                    mViewModel.onGetDataSuccess(statuses);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDataFailed(error.getMessage());
                    mViewModel.hideProgress();
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgress();
                }
            });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getDeviceUsingHistoryStatus() {
        Disposable disposable = mDeviceUsingHistoryRepository.getListStatus()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgress();
                }
            })
            .subscribe(new Consumer<List<Status>>() {
                @Override
                public void accept(List<Status> statuses) throws Exception {
                    mViewModel.onGetDataSuccess(statuses);
                    mViewModel.hideProgress();
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDataFailed(error.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgress();
                }
            });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void loadMoreData() {
        if (mSelectionType == STATUS
            || mSelectionType == CATEGORY
            || mSelectionType == DEVICE_USING_HISTORY
            || mSelectionType == BRANCH
            || mSelectionType == DEVICE_GROUP
            || mSelectionType == STATUS_REQUEST
            || mSelectionType == RELATIVE_STAFF) {
            mViewModel.onGetDataFailed(null);
            mViewModel.hideProgress();
            return;
        }
        mPage++;
        switch (mSelectionType) {
            case VENDOR:
                getListVendor();
                break;
            case MARKER:
                getListMarker();
                break;
            case MEETING_ROOM:
                getListMeetingRoom();
                break;
            default:
                break;
        }
    }

    @Override
    public void getListMarker() {
        Disposable disposable = mMarkerRepository.getListMarker(mKeySearch, mPage, PER_PAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgress();
                }
            })
            .subscribe(new Consumer<List<Producer>>() {
                @Override
                public void accept(List<Producer> statuses) throws Exception {
                    if (TextUtils.isEmpty(mKeySearch) && statuses != null && statuses.size() != 0) {
                        statuses.add(0, (Producer) new Producer(OUT_OF_INDEX, TITLE_NA));
                    }
                    mViewModel.onGetDataSuccess(statuses);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDataFailed(error.getMessage());
                    mViewModel.hideProgress();
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgress();
                }
            });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getListMeetingRoom() {
        Disposable disposable =
            mMeetingRoomRepository.getListMeetingRoom(mKeySearch, mPage, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mViewModel.showProgress();
                    }
                })
                .subscribe(new Consumer<List<Producer>>() {
                    @Override
                    public void accept(List<Producer> statuses) throws Exception {
                        if (TextUtils.isEmpty(mKeySearch)
                            && statuses != null
                            && statuses.size() != 0) {
                            statuses.add(0, (Producer) new Producer(OUT_OF_INDEX, TITLE_NA));
                        }
                        mViewModel.onGetDataSuccess(statuses);
                        mViewModel.hideProgress();
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onGetDataFailed(error.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mViewModel.hideProgress();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getListVendor() {
        Disposable disposable = mVendorRepository.getListVendor(mKeySearch, mPage, PER_PAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgress();
                }
            })
            .subscribe(new Consumer<List<Producer>>() {
                @Override
                public void accept(List<Producer> statuses) throws Exception {
                    if (TextUtils.isEmpty(mKeySearch) && statuses != null && statuses.size() != 0) {
                        statuses.add(0, (Producer) new Producer(OUT_OF_INDEX, TITLE_NA));
                    }
                    mViewModel.onGetDataSuccess(statuses);
                    mViewModel.hideProgress();
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDataFailed(error.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgress();
                }
            });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getListCategory() {
        Observable<List<Producer>> observable;
        final String titleFirstItem;
        final boolean isInsertFirstItem;
        if (mDeviceGroupId != 0) {
            observable = mCategoryRepository.getListCategory(mKeySearch, mDeviceGroupId);
            titleFirstItem = TITLE_ALL;
            isInsertFirstItem = mDeviceGroupId == OUT_OF_INDEX;
        } else {
            observable = mCategoryRepository.getListCategory(mKeySearch);
            titleFirstItem = TITLE_NA;
            isInsertFirstItem = true;
        }
        Disposable disposable =
            observable.subscribeOn(Schedulers.io()).doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgress();
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Producer>>() {
                @Override
                public void accept(List<Producer> producers) throws Exception {
                    if (TextUtils.isEmpty(mKeySearch)
                        && producers != null
                        && producers.size() != 0
                        && isInsertFirstItem) {
                        producers.add(0, new Producer(OUT_OF_INDEX, titleFirstItem));
                    }
                    mViewModel.onGetDataSuccess(producers);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDataFailed(error.getMessage());
                    mViewModel.hideProgress();
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgress();
                }
            });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getListStatus() {
        Disposable disposable = mStatusRepository.getListStatus(mKeySearch)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgress();
                }
            })
            .subscribe(new Consumer<List<Status>>() {
                @Override
                public void accept(List<Status> statuses) throws Exception {
                    if (TextUtils.isEmpty(mKeySearch) && statuses != null && statuses.size() != 0) {
                        statuses.add(0, new Status(OUT_OF_INDEX, TITLE_NA));
                    }
                    mViewModel.onGetDataSuccess(statuses);
                    mViewModel.hideProgress();
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDataFailed(error.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgress();
                }
            });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getDeviceGroups() {
        Disposable disposable = mDeviceGroupRepository.getListDeviceGroup(mKeySearch)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgress();
                }
            })
            .subscribe(new Consumer<List<Producer>>() {
                @Override
                public void accept(@NonNull List<Producer> statuses) throws Exception {
                    if (TextUtils.isEmpty(mKeySearch) && statuses != null && statuses.size() != 0) {
                        statuses.add(0, new Producer(OUT_OF_INDEX, TITLE_ALL));
                    }
                    mViewModel.onGetDataSuccess(statuses);
                    mViewModel.hideProgress();
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDataFailed(error.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgress();
                }
            });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getListBranch() {
        Disposable disposable = mBranchRepository.getListBranch()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgress();
                }
            })
            .subscribe(new Consumer<List<Status>>() {
                @Override
                public void accept(@NonNull List<Status> statuses) throws Exception {
                    mViewModel.onGetDataSuccess(statuses);
                    mViewModel.hideProgress();
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDataFailed(error.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgress();
                }
            });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getListAssignee() {
        Disposable disposable = mStatusRepository.getListAssignee()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgress();
                }
            })
            .subscribe(new Consumer<List<Status>>() {
                @Override
                public void accept(@NonNull List<Status> statuses) throws Exception {
                    statuses.add(0, new Status(OUT_OF_INDEX, NONE));
                    mViewModel.onGetDataSuccess(statuses);
                    mViewModel.hideProgress();
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDataFailed(error.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgress();
                }
            });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getListUserBorrow() {
        Disposable disposable = mStatusRepository.getListUserBorrow()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    mViewModel.showProgress();
                }
            })
            .subscribe(new Consumer<List<Status>>() {
                @Override
                public void accept(@NonNull List<Status> statuses) throws Exception {
                    statuses.add(0, new Status(OUT_OF_INDEX, NONE));
                    mViewModel.onGetDataSuccess(statuses);
                    mViewModel.hideProgress();
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onGetDataFailed(error.getMessage());
                    mViewModel.hideProgress();
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    mViewModel.hideProgress();
                }
            });
        mCompositeDisposable.add(disposable);
    }
}
