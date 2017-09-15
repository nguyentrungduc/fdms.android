package com.framgia.fdms.screen.new_selection;

import android.text.TextUtils;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.CategoryRepository;
import com.framgia.fdms.data.source.MarkerRepository;
import com.framgia.fdms.data.source.MeetingRoomRepository;
import com.framgia.fdms.data.source.StatusRepository;
import com.framgia.fdms.data.source.VendorRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

import static com.framgia.fdms.screen.new_selection.SelectionType.CATEGORY;
import static com.framgia.fdms.screen.new_selection.SelectionType.MARKER;
import static com.framgia.fdms.screen.new_selection.SelectionType.MEETING_ROOM;
import static com.framgia.fdms.screen.new_selection.SelectionType.STATUS;
import static com.framgia.fdms.screen.new_selection.SelectionType.VENDOR;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.PER_PAGE;
import static com.framgia.fdms.utils.Constant.TITLE_NA;

/**
 * Listens to user actions from the UI ({@link StatusSelectionActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
public final class StatusSelectionPresenter implements StatusSelectionContract.Presenter {

    private final StatusSelectionContract.ViewModel mViewModel;
    @SelectionType
    private int mSelectionType;
    private CompositeDisposable mCompositeDisposable;
    private StatusRepository mStatusRepository;
    private CategoryRepository mCategoryRepository;
    private VendorRepository mVendorRepository;
    private MarkerRepository mMarkerRepository;
    private MeetingRoomRepository mMeetingRoomRepository;
    private int mPage = 1;
    private String mKeySearch;

    public StatusSelectionPresenter(StatusSelectionContract.ViewModel viewModel,
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

    public void setMeetingRoomRepository(MeetingRoomRepository meetingRoomRepository) {
        mMeetingRoomRepository = meetingRoomRepository;
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
            case MEETING_ROOM:
                getListMeetingRoom();
                break;
            default:
                break;
        }
    }

    @Override
    public void loadMoreData() {
        if (mSelectionType == STATUS || mSelectionType == CATEGORY) {
            mViewModel.onGetDataFailed(null);
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

    private void getListMarker() {
        Disposable disposable = mMarkerRepository.getListMarker(mKeySearch, mPage, PER_PAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
                }
            });
        mCompositeDisposable.add(disposable);
    }

    private void getListMeetingRoom() {
        Disposable disposable =
            mMeetingRoomRepository.getListMeetingRoom(mKeySearch, mPage, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Producer>>() {
                    @Override
                    public void accept(List<Producer> statuses) throws Exception {
                        if (TextUtils.isEmpty(mKeySearch)
                            && statuses != null
                            && statuses.size() != 0) {
                            statuses.add(0, (Producer) new Producer(OUT_OF_INDEX, TITLE_NA));
                        }
                        mViewModel.onGetDataSuccess(statuses);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onGetDataFailed(error.getMessage());
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private void getListVendor() {
        Disposable disposable = mVendorRepository.getListVendor(mKeySearch, mPage, PER_PAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
                }
            });
        mCompositeDisposable.add(disposable);
    }

    private void getListCategory() {
        Disposable disposable = mCategoryRepository.getListCategory(mKeySearch)
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
                }
            });
        mCompositeDisposable.add(disposable);
    }

    private void getListStatus() {
        Disposable disposable = mStatusRepository.getListStatus(mKeySearch)
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
                }
            });
        mCompositeDisposable.add(disposable);
    }
}
