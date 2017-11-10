package com.framgia.fdms.screen.device.listdevice.selectmaker;

import android.text.TextUtils;

import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.source.MarkerRepository;
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
public class SelectMakerPresenter extends BaseSelectionPresenter {

    private MarkerRepository mMarkerRepository;


    public SelectMakerPresenter(BaseSelectionContract.ViewModel viewModel,
                                MarkerRepository markerRepository) {
        super(viewModel);
        mMarkerRepository = markerRepository;
    }

    @Override
    public void getData(String query) {
        mKeySearch = query;
        mPage = FIRST_PAGE;
        getData(mKeySearch, mPage);
    }

    private void getData(final String query, int page) {
        Disposable disposable = mMarkerRepository
                .getListMarker(query, page, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Producer>>() {
                    @Override
                    public void accept(List<Producer> producers) throws Exception {
                        if (mPage== FIRST_PAGE && TextUtils.isEmpty(query)){
                            producers.add(0, new Producer(OUT_OF_INDEX,
                                    mViewModel.getString(R.string.action_all)));
                            mViewModel.clearData();
                        }

                        mViewModel.onGetDataSuccess(producers);
                        mViewModel.setAllowLoadMore(producers.size() >= PER_PAGE);
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
        mPage++;
        getData(mKeySearch, mPage);
    }

}
