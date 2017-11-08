package com.framgia.fdms.screen.baseselection;

import io.reactivex.disposables.CompositeDisposable;

import static com.framgia.fdms.utils.Constant.FIRST_PAGE;

/**
 * Listens to user actions from the UI ({@link BaseSelectionActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
public abstract class BaseSelectionPresenter implements BaseSelectionContract.Presenter {

    protected BaseSelectionContract.ViewModel mViewModel;
    protected CompositeDisposable mCompositeDisposable;
    protected int mPage = FIRST_PAGE;
    protected String mKeySearch;

    public abstract void getData(String query);

    public abstract void loadMoreData();

    public BaseSelectionPresenter(BaseSelectionContract.ViewModel viewModel) {
        mViewModel = viewModel;
        mCompositeDisposable = new CompositeDisposable();
    }


    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mCompositeDisposable.clear();
    }


}
