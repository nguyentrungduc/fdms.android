package com.framgia.fdms.screen.baseselection;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface BaseSelectionContract {
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

        void setAllowLoadMore(boolean allowLoadMore);

        String getString(int resourceId);

        void clearData();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getData(String query);

        void loadMoreData();

    }
}
