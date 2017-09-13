package com.framgia.fdms.screen.producer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.framgia.fdms.BR;
import com.framgia.fdms.FDMSApplication;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.utils.navigator.Navigator;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.TAG_MAKER_DIALOG;

/**
 * Exposes the data to be used in the Vendor screen.
 */
public class ProducerViewModel extends BaseObservable
    implements ProducerContract.ViewModel, ProducerDialogContract.ActionCallback,
    FloatingSearchView.OnSearchListener, FloatingSearchView.OnClearSearchActionListener {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProducerViewModel> CREATOR =
        new Parcelable.Creator<ProducerViewModel>() {
            @Override
            public ProducerViewModel createFromParcel(Parcel in) {
                return new ProducerViewModel(in);
            }

            @Override
            public ProducerViewModel[] newArray(int size) {
                return new ProducerViewModel[size];
            }
        };
    private ProducerFunctionContract.ProducerPresenter mPresenter;
    private ProducerAdapter mAdapter;
    private AppCompatActivity mActivity;
    private ProducerDialog mVendorDialog;
    private int mPositionScroll = OUT_OF_INDEX;
    private Producer mVendorEdit;
    private boolean mIsLoadMore;
    private int mLoadingMoreVisibility;
    private Navigator mNavigator;

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy <= 0) {
                return;
            }
            LinearLayoutManager layoutManager =
                (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
            if (!mIsLoadMore && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                mIsLoadMore = true;
                setLoadingMoreVisibility(View.VISIBLE);
                ((ProducerPresenter) mPresenter).loadMorePage();
            }
        }
    };

    public ProducerViewModel(Activity activity) {
        mActivity = (AppCompatActivity) activity;
        mNavigator = new Navigator(mActivity);
        mAdapter =
            new ProducerAdapter(FDMSApplication.getInstant(), this, new ArrayList<Producer>());
        setLoadingMoreVisibility(GONE);
    }

    protected ProducerViewModel(Parcel in) {
        mPresenter = (ProducerContract.Presenter) in.readValue(
            ProducerContract.Presenter.class.getClassLoader());
        mAdapter = (ProducerAdapter) in.readValue(ProducerAdapter.class.getClassLoader());
        mActivity = (AppCompatActivity) in.readValue(AppCompatActivity.class.getClassLoader());
        mVendorDialog = (ProducerDialog) in.readValue(ProducerDialog.class.getClassLoader());
    }

    @Bindable
    public int getLoadingMoreVisibility() {
        return mLoadingMoreVisibility;
    }

    public void setLoadingMoreVisibility(int visibility) {
        mLoadingMoreVisibility = visibility;
        notifyPropertyChanged(BR.loadingMoreVisibility);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(ProducerFunctionContract.ProducerPresenter presenter) {
        mPresenter = presenter;
    }

    @Bindable
    public ProducerAdapter getAdapter() {
        return mAdapter;
    }

    @Bindable
    public int getPositionScroll() {
        return mPositionScroll;
    }

    public void setPositionScroll(int positionScroll) {
        mPositionScroll = positionScroll;
        notifyPropertyChanged(BR.positionScroll);
    }

    @Override
    public void onLoadVendorSuccess(List<Producer> vendors) {
        mAdapter.onUpdatePage(vendors);
        mIsLoadMore = false;
        setLoadingMoreVisibility(GONE);
    }

    @Override
    public void onLoadVendorFailed() {
        Snackbar.make(mActivity.findViewById(android.R.id.content), R.string.msg_load_data_fails,
            Snackbar.LENGTH_SHORT).show();
        setLoadingMoreVisibility(GONE);
    }

    @Override
    public void onAddVendorFailed(String msg) {
        mNavigator.showToast(msg);
    }

    @Override
    public void onAddVendorSuccess(Producer vendor) {
        mAdapter.addData(0, vendor);
        setPositionScroll(0);
    }

    public void onDeleteVendorFailed(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void onDeleteVendorSuccess(Producer producer) {
        // no ops
        mAdapter.removeData(producer);
    }

    @Override
    public void onUpdateVendorSuccess(Producer vendor, String message) {
        if (vendor == null || mVendorEdit == null) {
            return;
        }
        mVendorEdit.setName(vendor.getName());
        mVendorEdit.setDescription(vendor.getDescription());
        mNavigator.showToast(message);
    }

    @Override
    public void onUpdateVendorFailed(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void showProgress() {
        setLoadingMoreVisibility(VISIBLE);
    }

    @Override
    public void hideProgress() {
        setLoadingMoreVisibility(GONE);
    }

    @Override
    public void onEditProducerClick(Producer vendor) {
        mVendorDialog = ProducerDialog.newInstant(vendor,
            mActivity.getResources().getString(R.string.action_edit), this);
        mVendorDialog.show(mActivity.getSupportFragmentManager(), TAG_MAKER_DIALOG);
    }

    @Override
    public void onDeleteProducerClick(final Producer vendor) {
        new AlertDialog.Builder(mActivity).setTitle(mActivity.getString(R.string.title_delete))
            .setMessage(mActivity.getString(R.string.msg_delete_producer) + " " + vendor.getName())
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((ProducerPresenter) mPresenter).deleteVendor(vendor);
                }
            })
            .setNegativeButton(android.R.string.no, null)
            .create()
            .show();
    }

    @Override
    public void onAddProducerClick() {
        mVendorDialog = ProducerDialog.newInstant(new Producer(),
            mActivity.getResources().getString(R.string.title_add_producer), this);
        mVendorDialog.show(mActivity.getSupportFragmentManager(), TAG_MAKER_DIALOG);
    }

    @Override
    public void onAddCallback(Producer producer) {
        if (producer == null) {
            return;
        }
        ((ProducerPresenter) mPresenter).addVendor(producer);
    }

    @Override
    public void onEditCallback(Producer oldProducer, Producer newProducer) {
        if (oldProducer == null || newProducer == null) {
            return;
        }
        mVendorEdit = oldProducer;
        ((ProducerContract.Presenter) mPresenter).editVendor(newProducer);
    }

    @Bindable
    public RecyclerView.OnScrollListener getScrollListener() {
        return mScrollListener;
    }

    @Override
    public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
        // no ops
    }

    @Override
    public void onSearchAction(String currentQuery) {
        mAdapter.clearData();
        ((ProducerPresenter) mPresenter).getVendors(currentQuery);
    }

    @Override
    public void onClearSearchClicked() {
        mAdapter.clearData();
        ((ProducerPresenter) mPresenter).getVendors("");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mPresenter);
        dest.writeValue(mAdapter);
        dest.writeValue(mActivity);
        dest.writeValue(mVendorDialog);
    }
}