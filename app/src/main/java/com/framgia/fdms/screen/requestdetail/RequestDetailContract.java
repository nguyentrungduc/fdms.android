package com.framgia.fdms.screen.requestdetail;

import android.content.Intent;

import com.framgia.fdms.BasePresenter;
import com.framgia.fdms.BaseViewModel;
import com.framgia.fdms.data.model.Category;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.User;

import java.util.List;

/**
 * Created by tuanbg on 5/24/17.
 */
public interface RequestDetailContract {
    interface ViewModel extends BaseViewModel<RequestDetailContract.Presenter> {
        void showProgressbar();
        void hideProgressbar();
        void onLoadError(String message);
        void onGetCategorySuccess(List<Category> categories);
        void onActivityResult(int requestCode, int resultCode, Intent data);
        void onSubmitEditClick();
        void onCancelEditClick();
        boolean onBackPressed();
        void initFloatActionButton(boolean isEdit);
        void onGetReponeSuccess(Respone<Request> requestRespone);
        void setCurrentUser(User user);
        void onUploadRequestError(String message);
        void initActionEdit(Request request);
    }

    interface Presenter extends BasePresenter {
        void updateActionRequest(int requestId, int actionId);
        void updateRequest(Request request);
        void getCurrentUser();
        void initFloatActionButton(Request request);
    }
}
