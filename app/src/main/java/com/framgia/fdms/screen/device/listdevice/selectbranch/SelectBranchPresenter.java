package com.framgia.fdms.screen.device.listdevice.selectbranch;

import android.text.TextUtils;

import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.BranchRepository;
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

import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * Listens to user actions from the UI ({@link BaseSelectionActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
public class SelectBranchPresenter extends BaseSelectionPresenter {

    private BranchRepository mBranchRepository;


    public SelectBranchPresenter(BaseSelectionContract.ViewModel viewModel,
                                 BranchRepository branchRepository) {
        super(viewModel);
        mBranchRepository = branchRepository;
    }

    @Override
    public void getData(final String query) {
        mKeySearch = query;
        Disposable disposable = mBranchRepository
                .getListBranch(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Status>>() {
                    @Override
                    public void accept(List<Status> branches) throws Exception {
                        if (TextUtils.isEmpty(query)) {
                            branches.add(0, new Status(OUT_OF_INDEX,
                                    mViewModel.getString(R.string.action_all)));
                            mViewModel.clearData();
                        }
                        mViewModel.onGetDataSuccess(branches);
                        mViewModel.setAllowLoadMore(false);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mViewModel.onGetDataFailed(error.getMessage());
                        mViewModel.hideProgress();
                        mPage--;
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
        mViewModel.hideProgress();
    }

}
