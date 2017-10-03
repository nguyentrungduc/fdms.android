package com.framgia.fdms.screen.producer;

import android.content.Intent;
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
        void onLoadProducerSuccess(List<Producer> producers);

        void onLoadProducerFailed(String msg);

        void onAddProducerFailed(String msg);

        void onAddProducerSuccess(Producer producer);

        void onDeleteProducerFailed(String message);

        void onDeleteProducerSuccess(Producer producer);

        void onUpdateProducerSuccess(Producer producer, String message);

        void onUpdateProducerFailed(String message);

        void showProgress();

        void hideProgress();

        void onEditProducerClick(Producer producer);

        void onDeleteProducerClick(Producer producer);

        void onAddProducerClick();

        void setAllowLoadMore(boolean isAllowLoadMore);

        void setShowCategoryFilter(boolean isShowCategoryFilter);

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getProducer();

        void loadMorePage();

        void addProducer(Producer producer, Producer temProductGroup);

        void deleteProducer(Producer producer);

        void editProducer(Producer producer, Producer temProductGroup);

        void getProducer(String name, int groupTypeId);
    }
}
