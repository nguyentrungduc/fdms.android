package com.framgia.fdms.screen.requestcreation.assignee;

import android.text.TextUtils;

import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.AssigneeDataSource;
import com.framgia.fdms.screen.baseselection.BaseSelectionActivity;
import com.framgia.fdms.screen.baseselection.BaseSelectionContract;
import com.framgia.fdms.screen.baseselection.BaseSelectionPresenter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * Listens to user actions from the UI ({@link BaseSelectionActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
public class SelectAssignRequestPresenter extends BaseSelectionPresenter {

    private AssigneeDataSource mAssigneeRepository;

    public SelectAssignRequestPresenter(BaseSelectionContract.ViewModel viewModel,
                                        AssigneeDataSource assigneeRepository) {
        super(viewModel);
        mAssigneeRepository = assigneeRepository;
    }

    @Override
    public void getData(String query) {
        mKeySearch = query;
        Disposable disposable = mAssigneeRepository
                .getListAssignee(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Status>>() {
                    @Override
                    public void accept(List<Status> statuses) throws Exception {
                        if (mPage == FIRST_PAGE && TextUtils.isEmpty(mKeySearch)) {
                            statuses.add(0,  new Status(OUT_OF_INDEX,
                                    mViewModel.getString(R.string.title_none)));
                            mViewModel.clearData();
                        }
                        mViewModel.onGetDataSuccess(statuses);
                        mViewModel.setAllowLoadMore(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mViewModel.hideProgress();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mViewModel.hideProgress();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void loadMoreData() {
        // no ops
        mViewModel.hideProgress();
    }

}
