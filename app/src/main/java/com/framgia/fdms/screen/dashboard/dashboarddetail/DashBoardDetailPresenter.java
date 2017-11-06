package com.framgia.fdms.screen.dashboard.dashboarddetail;

import com.framgia.fdms.data.model.Dashboard;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.DeviceRepository;
import com.framgia.fdms.data.source.RequestRepository;
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

import static com.framgia.fdms.screen.dashboard.dashboarddetail.DashBoardDetailFragment
        .DEVICE_DASHBOARD;
import static com.framgia.fdms.screen.dashboard.dashboarddetail.DashBoardDetailFragment
        .REQUEST_DASHBOARD;

/**
 * Listens to user actions from the UI ({@link DashBoardDetailFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
public final class DashBoardDetailPresenter implements DashBoardDetailContract.Presenter {
    public static final int top = 1;
    private final DashBoardDetailContract.ViewModel mViewModel;
    private CompositeDisposable mCompositeSubscriptions;
    private DeviceRepository mDeviceRepository;
    private RequestRepository mRequestRepository;
    private UserRepository mUserRepository;
    private int mDashboardType;

    public DashBoardDetailPresenter(DashBoardDetailContract.ViewModel viewModel,
                                    DeviceRepository deviceRepository, RequestRepository requestRepository, int dashboardType,
                                    UserRepository userRepository) {
        mViewModel = viewModel;
        mDeviceRepository = deviceRepository;
        mRequestRepository = requestRepository;
        mDashboardType = dashboardType;
        mUserRepository = userRepository;
        mCompositeSubscriptions = new CompositeDisposable();
        getCurrentUser();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mCompositeSubscriptions.clear();
    }

    @Override
    public void getDeviceDashboard() {
        Disposable subscription = mDeviceRepository.getDashboardDevice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Dashboard>>() {
                    @Override
                    public void accept(List<Dashboard> dashboards) throws Exception {
                        mViewModel.onDashBoardLoaded(dashboards);
                        mViewModel.setRefresh(false);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onDashBoardError(error.getMessage());
                    }
                });
        mCompositeSubscriptions.add(subscription);
    }

    @Override
    public void getRequestDashboard() {
        Disposable subscription = mRequestRepository.getDashboardRequest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Dashboard>>() {
                    @Override
                    public void accept(List<Dashboard> dashboards) throws Exception {
                        mViewModel.onDashBoardLoaded(dashboards);
                        mViewModel.setRefresh(false);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onDashBoardError(error.getMessage());
                    }
                });
        mCompositeSubscriptions.add(subscription);
    }

    @Override
    public void getData() {
        mViewModel.setRefresh(true);
        if (mDashboardType == DEVICE_DASHBOARD) {
            getDeviceDashboard();
            getTopDevice();
        } else if (mDashboardType == REQUEST_DASHBOARD) {
            getRequestDashboard();
            getTopRequest();
        }
    }

    @Override
    public void getTopRequest() {
        Disposable subscription = mRequestRepository.getTopRequest(top)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Request>>() {
                    @Override
                    public void accept(List<Request> requests) throws Exception {
                        mViewModel.onGetTopRequestSuccess(requests);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mViewModel.onDashBoardError(throwable.getMessage());
                    }
                });
        mCompositeSubscriptions.add(subscription);
    }

    @Override
    public void getTopDevice() {
        Disposable subscription = mDeviceRepository.getTopDevice(top)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Device>>() {
                    @Override
                    public void accept(List<Device> devices) throws Exception {
                        mViewModel.onGetTopDeviceSuccess(devices);
                        mViewModel.setRefresh(false);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onDashBoardError(error.getMessage());
                        mViewModel.setRefresh(false);
                    }
                });
        mCompositeSubscriptions.add(subscription);
    }

    @Override
    public void updateActionRequest(int requestId, int actionId) {
        Disposable subscription = mRequestRepository.updateActionRequest(requestId, actionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mViewModel.showProgressbar();
                    }
                })
                .subscribe(new Consumer<Respone<Request>>() {
                    @Override
                    public void accept(Respone<Request> requestRespone) throws Exception {
                        mViewModel.showMessage(requestRespone.getMessage());
                        mViewModel.onUpdateActionSuccess(requestRespone.getData());
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.hideProgressbar();
                        mViewModel.onDashBoardError(error.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() {
                        mViewModel.hideProgressbar();
                    }
                });

        mCompositeSubscriptions.add(subscription);
    }

    @Override
    public void cancelRequest(int reqeuestId, int actionId, String message) {
        Disposable subscription = mRequestRepository.cancelRequest(reqeuestId, actionId, message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mViewModel.showProgressbar();
                    }
                })
                .subscribe(new Consumer<Respone<Request>>() {
                    @Override
                    public void accept(Respone<Request> requestRespone) throws Exception {
                        mViewModel.showMessage(requestRespone.getMessage());
                        mViewModel.onUpdateActionSuccess(requestRespone.getData());
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.hideProgressbar();
                        mViewModel.onDashBoardError(error.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() {
                        mViewModel.hideProgressbar();
                    }
                });

        mCompositeSubscriptions.add(subscription);
    }

    @Override
    public void getCurrentUser() {
        Disposable subscription = mUserRepository.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        mViewModel.setCurrentUser(user);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onDashBoardError(error.getMessage());
                    }
                });
        mCompositeSubscriptions.add(subscription);
    }
}
