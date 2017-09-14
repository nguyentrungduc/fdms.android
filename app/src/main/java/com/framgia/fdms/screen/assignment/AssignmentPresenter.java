package com.framgia.fdms.screen.assignment;

import com.framgia.fdms.R;
import com.framgia.fdms.data.model.AssignmentRequest;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.CategoryRepository;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.RequestRepository;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link AssignmentActivity}), retrieves the data and updates
 * the UI as required.
 */
final class AssignmentPresenter implements AssignmentContract.Presenter {
    private static final String TAG = AssignmentPresenter.class.getName();

    private final AssignmentContract.ViewModel mViewModel;
    private int mRequestId;
    private RequestRepository mRequestRepository;
    private UserRepository mUserRepository;
    private CompositeDisposable mSubscription;
    private DeviceRepository mDeviceRepository;
    private CategoryRepository mCategoryRepository;

    public AssignmentPresenter(AssignmentContract.ViewModel viewModel, int requestId,
        RequestRepository requestRepository, UserRepository userRepository,
        DeviceRepository deviceRepository, CategoryRepository categoryRepository) {
        mViewModel = viewModel;
        mRequestId = requestId;
        mRequestRepository = requestRepository;
        mUserRepository = userRepository;
        mDeviceRepository = deviceRepository;
        mCategoryRepository = categoryRepository;
        mSubscription = new CompositeDisposable();
        getRequest(mRequestId);
        getDeviceGroups();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    @Override
    public void registerAssignment(AssignmentRequest request) {
        if (!validateAssignment(request)) {
            return;
        }
        Disposable subscription = mRequestRepository.registerAssignment(request)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Consumer<Request>() {
                @Override
                public void accept(Request request) throws Exception {
                    mViewModel.onAssignmentSuccess(request);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onLoadError(error.getMessage());
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void getRequest(int requestId) {
        Disposable subscription = mRequestRepository.getRequest(requestId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Consumer<Request>() {
                @Override
                public void accept(Request request) throws Exception {
                    mViewModel.onGetRequestSuccess(request);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onLoadError(error.getMessage());
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void chooseExportActivity() {
        Disposable subscription = mUserRepository.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<User>() {
                @Override
                public void accept(User user) throws Exception {
                    mViewModel.openChooseExportActivitySuccess(user);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onChooseExportActivityFailed();
                }
            });
        mSubscription.add(subscription);
    }

    @Override
    public void getDeviceGroups() {
        Disposable disposable = mDeviceRepository.getDeviceGroups()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<Status>>() {
                @Override
                public void accept(@NonNull List<Status> statuses) throws Exception {
                    mViewModel.onGetDeviceGroupsSuccess(statuses);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onLoadError(error.getMessage());
                }
            });
        mSubscription.add(disposable);
    }

    @Override
    public void getCategoriesByDeviceGroupId(int deviceGroupId) {
        Disposable disposable = mCategoryRepository.getCategoriesByDeviceGroupId(deviceGroupId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<Status>>() {
                @Override
                public void accept(@NonNull List<Status> statuses) throws Exception {
                    mViewModel.onGetCategoriesSuccess(statuses);
                }
            }, new RequestError() {
                @Override
                public void onRequestError(BaseException error) {
                    mViewModel.onLoadError(error.getMessage());
                }
            });
        mSubscription.add(disposable);
    }

    @Override
    public boolean validateAddItem(Status category, Device device, Status deviceGroup) {
        boolean isValid = true;
        if (category == null || device == null || deviceGroup == null) {
            isValid = false;
            mViewModel.onError(R.string.title_validate_item_assignment);
        }
        return isValid;
    }

    public boolean validateAssignment(AssignmentRequest request) {
        boolean isValid = true;
        if (request.getItemRequests().size() == 0) {
            isValid = false;
            mViewModel.onError(R.string.title_validate_assignment);
        }
        return isValid;
    }
}
