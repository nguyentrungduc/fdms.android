package com.framgia.fdms.screen.devicedetail.history;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.DeviceHistoryDetail;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface DeviceDetailHistoryContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetDeviceHistoryFailed(String message);

        void onGetDeviceHistorySuccess(List<DeviceHistoryDetail> deviceHistoryDetails);

        void showProgress();

        void hideProgress();

        void setAllowLoadMore(boolean isAllowLoadMore);

        void onLoadData();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getDetailHistory();

        void loadMoreData();
    }
}
