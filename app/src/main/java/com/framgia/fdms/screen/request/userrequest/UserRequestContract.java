package com.framgia.fdms.screen.request.userrequest;

import android.content.Intent;
import com.framgia.fdms.BaseFragmentContract;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.User;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface UserRequestContract {
    /**
     * View.
     */
    interface ViewModel extends BaseFragmentContract.ViewModel {
        void onGetRequestSuccess(List<Request> requests);

        void onLoadError(String msg);

        void getData();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onUpdateActionSuccess(Respone<Request> requestRespone);

        void refreshData();

        void setCurrentUser(User user);

        void setRefresh(boolean refresh);

        void onGetRequestError();

        void onRegisterRequestClick();

        void setAllowLoadMore(boolean allowLoadMore);

        void onActionRequestClick(int requestId, int actionId);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BaseFragmentContract.Presenter {
        void getMyRequest(int requestStatusId, int relativeId, int page, int perPage);

        void updateActionRequest(int requestId, int actionId);

        void cancelRequest(int requestId, int actionId, String description);

        void getCurrentUser();
    }
}
