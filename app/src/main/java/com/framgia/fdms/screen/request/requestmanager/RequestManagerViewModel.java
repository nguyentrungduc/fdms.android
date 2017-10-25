package com.framgia.fdms.screen.request.requestmanager;

import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.framgia.fdms.BR;
import com.framgia.fdms.BaseFragmentContract;
import com.framgia.fdms.BaseFragmentModel;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.Respone;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.screen.assignment.AssignmentActivity;
import com.framgia.fdms.screen.assignment.AssignmentType;
import com.framgia.fdms.screen.request.OnRequestClickListenner;
import com.framgia.fdms.screen.request.userrequest.UserRequestAdapter;
import com.framgia.fdms.screen.requestcreation.RequestCreationActivity;
import com.framgia.fdms.screen.requestdetail.RequestDetailActivity;
import com.framgia.fdms.screen.selection.SelectionActivity;
import com.framgia.fdms.utils.navigator.Navigator;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fdms.screen.selection.SelectionType.REQUEST_FOR;
import static com.framgia.fdms.screen.selection.SelectionType.STATUS_REQUEST_ALL;
import static com.framgia.fdms.screen.selection.SelectionViewModel.BUNDLE_DATA;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_RESPONE;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_SUCCESS;
import static com.framgia.fdms.utils.Constant.BundleRequestType.MEMBER_REQUEST;
import static com.framgia.fdms.utils.Constant.MANAGE_REQUEST_GROUP;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.RequestAction.CANCEL;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_CREATE_ASSIGNMENT;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_CREATE_REQUEST;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_DETAIL;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_SELECTION;
import static com.framgia.fdms.utils.Constant.RequestConstant.REQUEST_STATUS;

/**
 * Exposes the data to be used in the RequestManager screen.
 */

public class RequestManagerViewModel extends BaseFragmentModel
        implements RequestManagerContract.ViewModel, OnRequestClickListenner {

    private final Fragment mFragment;
    private Context mContext;
    private UserRequestAdapter mAdapter;

    private Status mStatus;
    private Status mRequestFor;
    private boolean mIsRefresh;
    private int mEmptyViewVisible = View.GONE; // show empty state when no date
    private SwipeRefreshLayout.OnRefreshListener mRefreshLayout =
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mAdapter.clear();
                    getData();
                }
            };
    private Navigator mNavigator;

    public RequestManagerViewModel(Fragment fragment) {
        mNavigator = new Navigator(fragment);
        mFragment = fragment;
        mContext = fragment.getContext();
        mAdapter = new UserRequestAdapter(mContext, new ArrayList<Request>(), this, new User());
        setStatus(new Status(OUT_OF_INDEX, mContext.getString(R.string.title_all_request_status)));
        setRequestFor(new Status(OUT_OF_INDEX, mContext.getString(R.string.title_all_request_for)));
    }

    @Override
    public void onStart() {
        if (mPresenter == null) return;
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    public void setPresenter(BaseFragmentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public UserRequestAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(UserRequestAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onGetRequestSuccess(List<Request> requests) {
        setLoadMore(false);
        setEmptyViewVisible(requests.isEmpty() ? View.VISIBLE : View.GONE);
        mAdapter.onUpdatePage(requests);
    }

    @Override
    public void onGetRequestError() {
        setLoadMore(false);
        setEmptyViewVisible(View.VISIBLE);
    }

    @Override
    public void onRegisterRequestClick() {
        mFragment.startActivityForResult(
                RequestCreationActivity.getInstance(mFragment.getActivity(), MEMBER_REQUEST),
                REQUEST_CREATE_REQUEST);
    }

    @Override
    public void setAllowLoadMore(boolean allowLoadMore) {
        mIsAllowLoadMore = allowLoadMore;
    }

    @Override
    public void onLoadError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getData() {
        mPresenter.getData(null, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || data.getExtras() == null || resultCode != RESULT_OK) {
            return;
        }
        Bundle bundle = data.getExtras();
        Status dataResponse = bundle.getParcelable(BUNDLE_DATA);
        switch (requestCode) {
            case REQUEST_SELECTION:
                if (dataResponse == null) {
                    return;
                }
                if (dataResponse.getId() == OUT_OF_INDEX) {
                    dataResponse.setName(mContext.getString(R.string.title_all_request_for));
                }
                setRequestFor(dataResponse);
                mAdapter.clear();
                mPresenter.getData(mRequestFor, mStatus);
                break;
            case REQUEST_STATUS:
                if (dataResponse == null) {
                    return;
                }
                if (dataResponse.getId() == OUT_OF_INDEX) {
                    dataResponse.setName(mContext.getString(R.string.title_all_request_status));
                }
                setStatus(dataResponse);
                mAdapter.clear();
                mPresenter.getData(mRequestFor, mStatus);
                break;
            case REQUEST_DETAIL:
                Respone<Request> requestRespone =
                        (Respone<Request>) bundle.getSerializable(BUNDLE_RESPONE);
                if (requestRespone != null) {
                    onUpdateActionSuccess(requestRespone);
                }
                break;
            case REQUEST_CREATE_ASSIGNMENT:
                mNavigator.showToast(bundle.getInt(BUNDLE_SUCCESS));
                mAdapter.clear();
                getData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onUpdateActionSuccess(Respone<Request> requestRespone) {
        if (requestRespone == null || requestRespone.getData() == null) return;
        mAdapter.updateItem(requestRespone.getData());
        Snackbar.make(mFragment.getView(), requestRespone.getMessage(), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void refreshData() {
        mAdapter.clear();
        mPresenter.getData(mRequestFor, mStatus);
    }

    @Override
    public void setCurrentUser(User user) {
        if (user == null) return;
        mAdapter.updateUser(user);
    }

    public void onSelectStatusClick() {
        mFragment.startActivityForResult(
                SelectionActivity.getInstance(mContext, STATUS_REQUEST_ALL), REQUEST_STATUS);
    }

    public void onSelectRequestForClick() {
        mFragment.startActivityForResult(SelectionActivity.getInstance(mContext, REQUEST_FOR),
                REQUEST_SELECTION);
    }

    @Bindable
    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public Status getRequestFor() {
        return mRequestFor;
    }

    public void setRequestFor(Status requestFor) {
        mRequestFor = requestFor;
        notifyPropertyChanged(BR.requestFor);
    }

    @Override
    public void onMenuClick(View v, final UserRequestAdapter.RequestModel request) {
        if (request == null
                || request.getRequest() == null
                || request.getRequest().getRequestActionList() == null) {
            return;
        }

        Request requestModel = request.getRequest();
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        for (int i = 0; i < requestModel.getRequestActionList().size(); i++) {
            final Request.RequestAction action = requestModel.getRequestActionList().get(i);
            popupMenu.getMenu()
                    .add(action.getName())
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            onActionRequestClick(request.getRequest().getId(), action.getId());
                            return false;
                        }
                    });
        }
        popupMenu.show();
    }

    @Override
    public void onActionRequestClick(final int reqeuestId, final int actionId) {
        switch (actionId) {
            case CANCEL:
                new LovelyTextInputDialog(mNavigator.getContext()).setTopColorRes(
                        R.color.colorPrimary)
                        .setTitle(R.string.msg_cancel_request)
                        .setIcon(R.drawable.ic_error_white)
                        .setConfirmButton(android.R.string.ok,
                                new LovelyTextInputDialog.OnTextInputConfirmListener() {
                                    @Override
                                    public void onTextInputConfirmed(String text) {
                                        if (TextUtils.isEmpty(text)) {
                                            return;
                                        }
                                        ((RequestManagerContract.Presenter) mPresenter).cancelRequest(
                                                reqeuestId, actionId, text);
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel, null)
                        .setInputFilter(R.string.error_empty_description,
                                new LovelyTextInputDialog.TextFilter() {
                                    @Override
                                    public boolean check(String s) {
                                        // TODO: 10/23/2017 check later
                                        return !TextUtils.isEmpty(s);
                                    }
                                })
                        .show();
                break;
            default:
                ((RequestManagerContract.Presenter) mPresenter).updateActionRequest(reqeuestId,
                        actionId);
                break;
        }
    }

    @Override
    public void onDetailRequestClick(Request request) {
        mFragment.startActivityForResult(
                RequestDetailActivity.getInstance(mContext, request),
                REQUEST_DETAIL);
    }

    @Override
    public void onAddDeviceClick(int requestId) {
        mFragment.startActivityForResult(
                AssignmentActivity.getInstance(mContext, AssignmentType.ASSIGN_BY_REQUEST, requestId),
                REQUEST_CREATE_ASSIGNMENT);
    }

    @Bindable
    public boolean getIsRefresh() {
        return mIsRefresh;
    }

    @Override
    public void setRefresh(boolean isRefresh) {
        mIsRefresh = isRefresh;
        notifyPropertyChanged(BR.isRefresh);
    }

    public SwipeRefreshLayout.OnRefreshListener getRefreshLayout() {
        return mRefreshLayout;
    }

    @Bindable
    public int getEmptyViewVisible() {
        return mEmptyViewVisible;
    }

    public void setEmptyViewVisible(int emptyViewVisible) {
        mEmptyViewVisible = emptyViewVisible;
        notifyPropertyChanged(BR.emptyViewVisible);
    }
}
