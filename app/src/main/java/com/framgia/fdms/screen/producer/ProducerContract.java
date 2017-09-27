package com.framgia.fdms.screen.producer;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Producer;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ProducerContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onLoadVendorSuccess(List<Producer> vendors);

        void onLoadVendorFailed(String msg);

        void onAddVendorFailed(String msg);

        void onAddVendorSuccess(Producer vendor);

        void onDeleteVendorFailed(String message);

        void onDeleteVendorSuccess(Producer vendor);

        void onUpdateVendorSuccess(Producer vendor, String message);

        void onUpdateVendorFailed(String message);

        void showProgress();

        void hideProgress();

        void onEditProducerClick(Producer producer);

        void onDeleteProducerClick(Producer producer);

        void onAddProducerClick();

        void setAllowLoadMore(boolean isAllowLoadMore);

        void setShowCategoryFilter(boolean isShowCategoryFilter);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getVendors();

        void loadMorePage();

        void addVendor(Producer producer);

        void deleteVendor(Producer producer);

        void editVendor(Producer producer);

        void getVendors(String name);
    }
}
