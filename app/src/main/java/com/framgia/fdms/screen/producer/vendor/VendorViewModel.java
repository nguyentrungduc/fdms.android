package com.framgia.fdms.screen.producer.vendor;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.framgia.fdms.BR;
import com.framgia.fdms.FDMSApplication;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.screen.producer.ProducerDialog;
import com.framgia.fdms.screen.producer.ProducerDialogContract;
import com.framgia.fdms.screen.producer.ProducerFunctionContract;
import com.framgia.fdms.utils.navigator.Navigator;
import java.util.ArrayList;
import java.util.List;

import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.TAG_MAKER_DIALOG;

/**
 * Exposes the data to be used in the Vendor screen.
 */
public class VendorViewModel extends BaseObservable
    implements VendorContract.ViewModel, ProducerDialogContract.ActionCallback {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VendorViewModel> CREATOR =
        new Parcelable.Creator<VendorViewModel>() {
            @Override
            public VendorViewModel createFromParcel(Parcel in) {
                return new VendorViewModel(in);
            }

            @Override
            public VendorViewModel[] newArray(int size) {
                return new VendorViewModel[size];
            }
        };
    private ProducerFunctionContract.ProducerPresenter mPresenter;
    private VendorAdapter mAdapter;
    private List<Producer> mVendors = new ArrayList<>();
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
                ((VendorPresenter) mPresenter).loadMorePage();
            }
        }
    };

    public VendorViewModel(Activity activity) {
        mActivity = (AppCompatActivity) activity;
        mNavigator = new Navigator(mActivity);
        mAdapter = new VendorAdapter(FDMSApplication.getInstant(), this, mVendors);
        setLoadingMoreVisibility(View.GONE);
    }

    protected VendorViewModel(Parcel in) {
        mPresenter = (VendorContract.Presenter) in.readValue(
            VendorContract.Presenter.class.getClassLoader());
        mAdapter = (VendorAdapter) in.readValue(VendorAdapter.class.getClassLoader());
        if (in.readByte() == 0x01) {
            mVendors = new ArrayList<Producer>();
            in.readList(mVendors, Producer.class.getClassLoader());
        } else {
            mVendors = null;
        }
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
    public VendorAdapter getAdapter() {
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
        if (vendors != null) {
            mVendors.addAll(vendors);
            mAdapter.notifyDataSetChanged();
        }
        mIsLoadMore = false;
        setLoadingMoreVisibility(View.GONE);
    }

    @Override
    public void onLoadVendorFailed() {
        Snackbar.make(mActivity.findViewById(android.R.id.content), R.string.msg_load_data_fails,
            Snackbar.LENGTH_SHORT).show();
        setLoadingMoreVisibility(View.GONE);
    }

    @Override
    public void onAddVendorFailed(String msg) {
        mNavigator.showToast(msg);
    }

    @Override
    public void onAddVendorSuccess(Producer vendor) {
        mVendors.add(0, vendor);
        mAdapter.notifyItemInserted(0);
        setPositionScroll(0);
    }

    public void onDeleteVendorFailed(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void onDeleteVendorSuccess(Producer producer) {
        // no ops
        mVendors.remove(producer);
        mAdapter.notifyItemRemoved(mVendors.indexOf(producer));
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
    public void onEditProducerClick(Producer vendor) {
        mVendorDialog = ProducerDialog.newInstant(vendor,
            mActivity.getResources().getString(R.string.action_edit), this);
        mVendorDialog.show(mActivity.getSupportFragmentManager(), TAG_MAKER_DIALOG);
    }

    @Override
    public void onDeleteProducerClick(Producer vendor) {
        ((VendorPresenter) mPresenter).deleteVendor(vendor);
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
        ((VendorPresenter) mPresenter).addVendor(producer);
    }

    @Override
    public void onEditCallback(Producer oldProducer, Producer newProducer) {
        if (oldProducer == null || newProducer == null) {
            return;
        }
        mVendorEdit = oldProducer;
        ((VendorContract.Presenter) mPresenter).editVendor(newProducer);
    }

    @Bindable
    public RecyclerView.OnScrollListener getScrollListener() {
        return mScrollListener;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mPresenter);
        dest.writeValue(mAdapter);
        if (mVendors == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mVendors);
        }
        dest.writeValue(mActivity);
        dest.writeValue(mVendorDialog);
    }

}