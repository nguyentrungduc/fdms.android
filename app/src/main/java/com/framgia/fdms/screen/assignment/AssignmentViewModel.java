package com.framgia.fdms.screen.assignment;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.data.model.User;
import com.framgia.fdms.data.source.UserRepository;
import com.framgia.fdms.data.source.local.UserLocalDataSource;
import com.framgia.fdms.data.source.local.sharepref.SharePreferenceImp;
import com.framgia.fdms.screen.profile.chooseexport.ChooseExportActivity;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.framgia.fdms.utils.Utils.hideSoftKeyboard;

/**
 * Exposes the data to be used in the Assignment screen.
 */
public class AssignmentViewModel extends BaseObservable implements AssignmentContract.ViewModel {
    private AssignmentContract.Presenter mPresenter;
    private AppCompatActivity mActivity;
    private int mProgressBarVisibility = View.GONE;
    private Request mRequest;
    private AssignmentAdapter mAdapter;
    private Context mContext;
    private UserRepository mUserRepository;
    private CompositeSubscription mSubscription;

    public AssignmentViewModel(AppCompatActivity activity) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
        mAdapter = new AssignmentAdapter(mContext, this);
        mUserRepository =
            new UserRepository(new UserLocalDataSource(new SharePreferenceImp(mContext)));
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
        mSubscription.clear();
    }

    @Override
    public void setPresenter(AssignmentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onAddItemClick() {
        hideSoftKeyboard(mActivity);
        mAdapter.addItem();
    }

    @Override
    public void onSaveClick() {
        // TODO: 19/06/2017
    }

    @Override
    public void onLoadError(String msg) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
            .show();
    }

    @Override
    public void onGetRequestSuccess(Request request) {
        if (request == null) return;
        setRequest(request);
    }

    @Override
    public void openChooseExportActivity() {
        Subscription subscription = mUserRepository.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<User>() {
                @Override
                public void call(User user) {
                    mActivity.startActivity(ChooseExportActivity.newInstance(mContext, user));
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Snackbar.make(mActivity.findViewById(android.R.id.content), R.string
                        .error_authentication, Snackbar.LENGTH_LONG).show();
                }
            });
        mSubscription.add(subscription);
    }

    public AppCompatActivity getActivity() {
        return mActivity;
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
    public AssignmentAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(AssignmentAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }
}
