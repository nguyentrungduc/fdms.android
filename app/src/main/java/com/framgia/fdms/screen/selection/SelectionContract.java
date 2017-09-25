package com.framgia.fdms.screen.selection;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface SelectionContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void setPresenter(Presenter presenter);

        void onSearch(String query);

        void onGetDataSuccess(List data);

        void onGetDataFailed(String msg);

        void showProgress();

        void hideProgress();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getData(String query);

        void loadMoreData();

        void getDeviceUsingHistoryStatus();

        void getListMarker();

        void getListMeetingRoom();

        void getListVendor();

        void getListCategory();

        void getListStatus();

        void getListStatusRequest();

        void getDeviceGroups();

        void getListBranch();

        void getListRelative();

        void getListAssignee();

        void getListUserBorrow();
    }
}
