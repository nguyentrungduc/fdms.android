package com.framgia.fdms.screen.requestcreation.requestfor;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.AssigneeDataSource;
import com.framgia.fdms.data.source.StatusRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
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
import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * Listens to user actions from the UI ({@link BaseSelectionActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
public class SelectRequestForPresenter extends BaseSelectionPresenter {

    private StatusRepository mAssigneeRepository;


    public SelectRequestForPresenter(BaseSelectionContract.ViewModel viewModel,
                                     StatusRepository assigneeRepository) {
        super(viewModel);
        mAssigneeRepository = assigneeRepository;
    }

    @Override
    public void getData(String query) {
        mKeySearch = query;
        mPage = FIRST_PAGE;
        getData(mKeySearch, mPage);
    }

    @SuppressLint("LongLogTag")
    public void getData(String query, final int page) {
        Disposable subscription = mAssigneeRepository
                .getListRelative(query, page, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Status>>() {
                    @Override
                    public void accept(List<Status> statuses) throws Exception {
                        if (page == FIRST_PAGE && TextUtils.isEmpty(mKeySearch)) {
                            statuses.add(0, new Status(OUT_OF_INDEX,
                                    mViewModel.getString(R.string.title_none)));
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

    @Override
    public void loadMoreData() {
        mPage++;
        getData(mKeySearch, mPage);
    }

}
