package com.framgia.fdms.screen.requestdetail.information;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import com.android.databinding.library.baseAdapters.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.screen.assignment.AssignmentActivity;
import com.framgia.fdms.screen.selection.SelectionActivity;
import com.framgia.fdms.screen.selection.SelectionType;
import com.framgia.fdms.utils.Constant;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fdms.screen.assignment.AssignmentType.ASSIGN_BY_REQUEST;
import static com.framgia.fdms.screen.requestdetail.information.RequestInformationViewModel
    .RequestStatusType.APPROVED_ID;
import static com.framgia.fdms.screen.requestdetail.information.RequestInformationViewModel
    .RequestStatusType.CANCELLED_ID;
import static com.framgia.fdms.screen.requestdetail.information.RequestInformationViewModel
    .RequestStatusType.DONE_ID;
import static com.framgia.fdms.screen.requestdetail.information.RequestInformationViewModel
    .RequestStatusType.WAITING_APPROVE_ID;
import static com.framgia.fdms.screen.requestdetail.information.RequestInformationViewModel
    .RequestStatusType.WAITING_DONE_ID;
import static com.framgia.fdms.screen.selection.SelectionType.EDIT_STATUS_REQUEST;
import static com.framgia.fdms.screen.selection.SelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_RESPONE;
import static com.framgia.fdms.utils.Constant.DeviceStatus.CANCELLED;
import static com.framgia.fdms.utils.Constant.DeviceStatus.WAITING_DONE;
import static com.framgia.fdms.utils.Constant.RequestAction.APPROVED;
import static com.framgia.fdms.utils.Constant.RequestAction.ASSIGNMENT;
import static com.framgia.fdms.utils.Constant.RequestAction.CANCEL;
import static com.framgia.fdms.utils.Constant.RequestAction.DONE;
import static com.framgia.fdms.utils.Constant.RequestAction.EDIT;
import static com.framgia.fdms.utils.Constant.RequestAction.RECEIVE;
import static com.framgia.fdms.utils.Constant.RequestAction.WAITING_APPROVE;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_ASSIGNEE;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_CREATE_ASSIGNMENT;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_RELATIVE;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_STATUS;
import static com.framgia.fdms.utils.Utils.hideSoftKeyboard;
import static com.github.clans.fab.FloatingActionButton.SIZE_MINI;

/**
 * Created by tuanbg on 5/24/17.
 */
public class RequestInformationViewModel extends BaseObservable
    implements RequestInformationContract.ViewModel {
    private Context mContext;
    private Fragment mFragment;
    private boolean mIsEdit;
    private RequestInformationContract.Presenter mPresenter;
    private List<Request.RequestAction> mListAction = new ArrayList<>();
    private String mStatusRequest;
    private Request mRequest;
    private int mProgressBarVisibility = View.GONE;
    private User mUser;
    private FloatingActionMenu mFloatingActionsMenu;
    private Request mRequestTemp;
    private int mActionMenuVisibility;
    private String mRequestTitleEmpty;
    private boolean isAcceptStatus;

    public RequestInformationViewModel(Fragment fragment, List<Request.RequestAction> actions,
        String statusRequest, Request actionRequest, FloatingActionMenu floatingActionMenu) {
        mContext = fragment.getContext();
        mFragment = fragment;
        setRequest(actionRequest);
        mStatusRequest = statusRequest;
        setEdit(false);
        mListAction.addAll(actions);
        mFloatingActionsMenu = floatingActionMenu;
    }

    @Override
    public void showProgressbar() {
        setProgressBarVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        setProgressBarVisibility(View.GONE);
    }

    @Override
    public void onLoadError(String message) {
        Snackbar.make(mFragment.getActivity().findViewById(android.R.id.content), message,
            Snackbar.LENGTH_LONG).show();
    }

    @Bindable
    public boolean isEdit() {
        return mIsEdit;
    }

    public void setEdit(boolean edit) {
        mIsEdit = edit;
        notifyPropertyChanged(BR.edit);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
    }

    @Override
    public void setPresenter(RequestInformationContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        Status status = bundle.getParcelable(BUNDLE_DATA);
        if (status == null || status.getId() < 1) {
            return;
        }
        switch (requestCode) {
            case REQUEST_STATUS:
                mRequest.setRequestStatus(status.getName());
                mRequest.setRequestStatusId(status.getId());
                break;
            case REQUEST_RELATIVE:
                mRequest.setRequestForId(status.getId());
                mRequest.setRequestFor(status.getName());
                break;
            case REQUEST_ASSIGNEE:
                mRequest.setAssigneeId(status.getId());
                mRequest.setAssignee(status.getName());
                break;
            default:
                break;
        }
    }

    @Override
    public void onSubmitEditClick() {
        mPresenter.updateRequest(mRequest);
        hideSoftKeyboard(mFragment.getActivity());
    }

    public void onChangeStatusClick() {
        if (!isEdit()) {
            return;
        }
        mFragment.startActivityForResult(
            SelectionActivity.newInstance(mContext, EDIT_STATUS_REQUEST,
                mRequest.getRequestStatusId()), REQUEST_STATUS);
    }

    public void onClickChooseRequestForRelativeStaff() {
        if (!isEdit() || Constant.DeviceStatus.APPROVED.equals(mRequest.getRequestStatus())) {
            return;
        }
        mFragment.startActivityForResult(
            SelectionActivity.getInstance(mContext, SelectionType.RELATIVE_STAFF),
            REQUEST_RELATIVE);
    }

    public void onClickAssignee() {
        mFragment.startActivityForResult(
            SelectionActivity.getInstance(mContext, SelectionType.ASSIGNEE), REQUEST_ASSIGNEE);
    }

    @Override
    public void onCancelEditClick() {
        setEdit(false);
        mFloatingActionsMenu.showMenu(true);
        if (mRequestTemp != null) {
            setRequest(mRequestTemp);
        }
        hideSoftKeyboard(mFragment.getActivity());
    }

    @Override
    public boolean onBackPressed() {
        if (isEdit()) {
            setEdit(false);
            return false;
        }
        return true;
    }

    public void initActionRequestMenu() {
        String requestStatus = mRequest.getRequestStatus();

        if (Constant.DeviceStatus.DONE.equals(requestStatus) || (!mUser.getName()
            .equals(mRequest.getCreater()) && (CANCELLED.equals(requestStatus)
            || WAITING_DONE.equals(requestStatus)))) {
            return;
        }
        Request.RequestAction editAction = new Request().new RequestAction();
        editAction.setId(EDIT);
        editAction.setName(mContext.getString(R.string.action_edit));
        mListAction.add(editAction);

        if (mUser.isBoStaff() && requestStatus.equals(WAITING_DONE) && mRequest.getId() > 0) {
            Request.RequestAction assignAction = new Request().new RequestAction();
            assignAction.setId(ASSIGNMENT);
            assignAction.setName(mContext.getString(R.string.title_assignment));
            mListAction.add(assignAction);
        }

        for (final Request.RequestAction requestAction : mListAction) {
            FloatingActionButton button = new FloatingActionButton(mContext);
            switch (requestAction.getId()) {
                case CANCEL:
                    button.setImageResource(R.drawable.ic_cancel_white_24px);
                    break;
                case WAITING_APPROVE:
                    button.setImageResource(R.drawable.ic_timer);
                    break;
                case APPROVED:
                    button.setImageResource(R.drawable.ic_done_white_24dp);
                    break;
                case RECEIVE:
                    button.setImageResource(R.drawable.ic_receive_24dp);
                    break;
                case DONE:
                    button.setImageResource(R.drawable.ic_done_white_24dp);
                    break;
                case EDIT:
                    button.setImageResource(R.drawable.ic_edit_white_24dp);
                    break;
                case ASSIGNMENT:
                    button.setImageResource(R.drawable.ic_assignment_white);
                    break;
                default:
                    break;
            }

            button.setLabelText(requestAction.getName());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onActionRequestClick(mRequest.getId(), requestAction.getId());
                }
            });
            button.setButtonSize(SIZE_MINI);
            mFloatingActionsMenu.addMenuButton(button);
        }
    }

    @Override
    public void onActionRequestClick(final int requestId, final int actionId) {
        switch (actionId) {
            case ASSIGNMENT:
                mFragment.getActivity()
                    .startActivityForResult(
                        AssignmentActivity.getInstance(mContext, ASSIGN_BY_REQUEST,
                            mRequest.getId()), REQUEST_CREATE_ASSIGNMENT);
                break;
            case EDIT:
                initEditRequest();
                break;
            case CANCEL:
                new LovelyTextInputDialog(mContext).setTopColorRes(R.color.colorPrimary)
                    .setTitle(R.string.msg_cancel_request)
                    .setIcon(R.drawable.ic_error_white)
                    .setConfirmButton(android.R.string.ok,
                        new LovelyTextInputDialog.OnTextInputConfirmListener() {
                            @Override
                            public void onTextInputConfirmed(String text) {
                                if (TextUtils.isEmpty(text)) {
                                    return;
                                }
                                mPresenter.cancelRequest(requestId, actionId, text);
                            }
                        })
                    .setNegativeButton(android.R.string.cancel, null)
                    .setInputFilter(R.string.error_empty_description,
                        new LovelyTextInputDialog.TextFilter() {
                            @Override
                            public boolean check(String s) {
                                // TODO: 10/23/2017 test later
                                return !TextUtils.isEmpty(s);
                            }
                        })
                    .show();
                break;
            default:
                mPresenter.updateActionRequest(requestId, actionId);
                break;
        }
    }

    @Override
    public void onRequestTitleEmpty() {
        setRequestTitleEmpty(mContext.getString(R.string.msg_error_user_name));
    }

    @Override
    public void hideActionRequestButton() {
        mFloatingActionsMenu.showMenu(true);
        setEdit(false);
    }

    @Override
    public void onGetReponeSuccess(Respone<Request> requestRespone) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_RESPONE, requestRespone);
        intent.putExtras(bundle);
        mFragment.getActivity().setResult(RESULT_OK, intent);
        mFragment.getActivity().finish();
    }

    @Override
    public void setCurrentUser(User user) {
        if (user == null) {
            return;
        }
        setUser(user);
        initActionRequestMenu();
        setActionMenuVisibility(
            mListAction != null && mListAction.size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onUploadRequestError(String message) {
        Snackbar.make(mFragment.getActivity().findViewById(android.R.id.content), message,
            Snackbar.LENGTH_LONG).show();
        onCancelEditClick();
    }

    public void initEditRequest() {
        setEdit(true);
        if (Constant.DeviceStatus.APPROVED.equals(mRequest.getRequestStatus())) {
            setAcceptStatus(true);
        }
        mFloatingActionsMenu.hideMenu(true);
        try {
            mRequestTemp = (Request) mRequest.clone();
            List<Request.DeviceRequest> list = new ArrayList<>();
            for (Request.DeviceRequest deviceRequest : mRequest.getDevices()) {
                list.add((Request.DeviceRequest) deviceRequest.clone());
            }
            mRequestTemp.setDevices(list);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public String getStatusRequest() {
        return mStatusRequest;
    }

    @Bindable
    public int getProgressBarVisibility() {
        return mProgressBarVisibility;
    }

    public void setProgressBarVisibility(int progressBarVisibility) {
        mProgressBarVisibility = progressBarVisibility;
        notifyPropertyChanged(BR.progressBarVisibility);
    }

    @Bindable
    public Request getRequest() {
        return mRequest;
    }

    public void setRequest(Request request) {
        mRequest = request;
        notifyPropertyChanged(BR.request);
    }

    @Bindable
    public int getActionMenuVisibility() {
        return mActionMenuVisibility;
    }

    public void setActionMenuVisibility(int actionMenuVisibility) {
        mActionMenuVisibility = actionMenuVisibility;
        notifyPropertyChanged(BR.actionMenuVisibility);
    }

    @Bindable
    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
        notifyPropertyChanged(BR.user);
    }

    @Bindable
    public String getRequestTitleEmpty() {
        return mRequestTitleEmpty;
    }

    public void setRequestTitleEmpty(String requestTitleEmpty) {
        mRequestTitleEmpty = requestTitleEmpty;
        notifyPropertyChanged(BR.requestTitleEmpty);
    }

    @Bindable
    public boolean isAcceptStatus() {
        return isAcceptStatus;
    }

    public void setAcceptStatus(boolean acceptStatus) {
        isAcceptStatus = acceptStatus;
        notifyPropertyChanged(BR.acceptStatus);
    }

    @IntDef({
        CANCELLED_ID, WAITING_APPROVE_ID, APPROVED_ID, WAITING_DONE_ID, DONE_ID
    })
    public @interface RequestStatusType {
        int CANCELLED_ID = 1;
        int WAITING_APPROVE_ID = 2;
        int APPROVED_ID = 3;
        int WAITING_DONE_ID = 4;
        int DONE_ID = 5;
    }
}
