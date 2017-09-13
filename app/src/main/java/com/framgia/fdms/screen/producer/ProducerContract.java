package com.framgia.fdms.screen.producer;

import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.screen.producer.ProducerFunctionContract;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ProducerContract {
    /**
     * View.
     */
    interface ViewModel extends ProducerFunctionContract.ViewModel<Presenter> {
        void onLoadVendorSuccess(List<Producer> vendors);

        void onLoadVendorFailed();

        void onAddVendorFailed(String msg);

        void onAddVendorSuccess(Producer vendor);

        void onDeleteVendorFailed(String message);

        void onDeleteVendorSuccess(Producer vendor);

        void onUpdateVendorSuccess(Producer vendor, String message);

        void onUpdateVendorFailed(String message);

        void showProgress();

        void hideProgress();
    }

    /**
     * Presenter.
     */
    interface Presenter extends ProducerFunctionContract.ProducerPresenter {
        void getVendors();

        void loadMorePage();

        void addVendor(Producer producer);

        void deleteVendor(Producer producer);

        void editVendor(Producer producer);

        void getVendors(String name);
    }
}
