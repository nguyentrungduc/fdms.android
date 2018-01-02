package com.framgia.fdms.screen.requestcreation.requestfor;

import android.text.TextUtils;

import com.framgia.fdms.R;
import com.framgia.fdms.data.model.AssigneeUser;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.StatusRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Listens to user actions from the UI ({@link SelectRequestForActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
public class SelectRequestForPresenter implements SelectRequestForContract.Presenter {

    protected SelectRequestForContract.ViewModel mViewModel;
    protected CompositeDisposable mCompositeDisposable;
    protected int mPage = FIRST_PAGE;
    protected String mKeySearch;

    private StatusRepository mAssigneeRepository;


    public SelectRequestForPresenter(SelectRequestForContract.ViewModel viewModel,
                                     StatusRepository assigneeRepository) {
        mViewModel = viewModel;
        mCompositeDisposable = new CompositeDisposable();
        mAssigneeRepository = assigneeRepository;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mCompositeDisposable.clear();
    }

    @Override
    public void getData(String query) {
        mKeySearch = query;
        mPage = FIRST_PAGE;
        getData(mKeySearch, mPage);
    }

    public void getData(String query, final int page) {
        Disposable subscription = mAssigneeRepository
                .getListRelative(query, page, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AssigneeUser>>() {
                    @Override
                    public void accept(List<AssigneeUser> statuses) throws Exception {
                        if (page == FIRST_PAGE && TextUtils.isEmpty(mKeySearch)) {
                            AssigneeUser noneUser = new AssigneeUser(OUT_OF_INDEX,
                                    mViewModel.getString(R.string.title_none));
                            noneUser.setGroups(new ArrayList<Status>());
                            statuses.add(0, noneUser);
                            mViewModel.clearData();
                        }
                        mViewModel.onGetDataSuccess(statuses);
                        mViewModel.setAllowLoadMore(statuses != null && statuses.size() >= PER_PAGE);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onGetDataFailed(error.getMessage());
                        mViewModel.hideProgress();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mViewModel.hideProgress();
                    }
                });

        mCompositeDisposable.add(subscription);
    }

    public void loadMoreData(){
        mPage++;
        getData(mKeySearch, mPage);
    }

}
