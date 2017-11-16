package com.framgia.fdms.screen.assignment;

import com.framgia.fdms.R;
import com.framgia.fdms.data.model.AssignmentRequest;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.RequestRepository;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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

    AssignmentPresenter(AssignmentContract.ViewModel viewModel, @AssignmentType int assignmentType,
                        int requestId, RequestRepository requestRepository, UserRepository userRepository) {
        mViewModel = viewModel;
        mRequestId = requestId;
        mRequestRepository = requestRepository;
        mUserRepository = userRepository;
        mSubscription = new CompositeDisposable();
        if (assignmentType == AssignmentType.ASSIGN_BY_REQUEST) {
            getRequest(mRequestId);
        }
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mSubscription.clear();
    }

    @Override
    public void registerAssignmentForRequest(AssignmentRequest request) {
        if (!validateAssignment(request.getDevices())) {
            return;
        }
        Disposable subscription = mRequestRepository.registerAssignment(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Request>() {
                    @Override
                    public void accept(Request request) throws Exception {
                        mViewModel.onAssignmentSuccess();
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
    public void registerAssignmentForMember(Status staff, List<Device> requests) {
        if (!validateStaff(staff)) {
            return;
        }
        if (!validateAssignment(requests)) {
            return;
        }
        Disposable disposable = mRequestRepository.registerAssignment(staff.getId(), requests)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String status) throws Exception {
                        mViewModel.onAssignmentSuccess();
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
    public void registerAssignmentForMeetingRoom(Status room, List<Device> requests) {
        // TODO: 11/16/2017 assign for meeting room when api available
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

    public boolean validateAssignment(List<Device> items) {
        boolean isValid = true;
        if (items.size() == 0) {
            isValid = false;
            mViewModel.onError(R.string.title_validate_assignment);
        }
        return isValid;
    }

    public boolean validateStaff(Status staff) {
        boolean isValid = true;
        if (staff == null) {
            isValid = false;
            mViewModel.onError(R.string.msg_input_staff_error);
        }
        return isValid;
    }
}
