package com.framgia.fdms.screen.profile.chooseexport;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.source.DeviceReturnRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Created by tuanbg on 6/15/17.
 */

public class ChooseExportPresenter implements ChooseExportContract.Presenter {
    private ChooseExportContract.ViewModel mViewModel;

    public ChooseExportPresenter(ChooseExportContract.ViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
    }
}
