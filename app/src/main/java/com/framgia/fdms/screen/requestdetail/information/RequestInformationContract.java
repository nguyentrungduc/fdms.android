package com.framgia.fdms.screen.requestdetail.information;

import android.content.Intent;
import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.User;

/**
 * Created by tuanbg on 5/24/17.
 */
public interface RequestInformationContract {
    interface ViewModel extends BaseViewModel<RequestInformationContract.Presenter> {
        void showProgressbar();

        void hideProgressbar();

        void onLoadError(String message);

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onSubmitEditClick();

        void onCancelEditClick();

        boolean onBackPressed();

        void onGetReponeSuccess(Respone<Request> requestRespone);

        void setCurrentUser(User user);

        void onUploadRequestError(String message);

        void onActionRequestClick(int requestId, int actionId);
    }

    interface Presenter extends BasePresenter {
        void updateActionRequest(int requestId, int actionId);

        void updateRequest(Request request);

        void getCurrentUser();
    }
}
