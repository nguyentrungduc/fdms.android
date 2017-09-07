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
import android.widget.Toast;

import com.framgia.fdms.BR;
import com.framgia.fdms.FDMSApplication;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.screen.producer.ProducerDialog;
import com.framgia.fdms.screen.producer.ProducerDialogContract;
import com.framgia.fdms.screen.producer.ProducerFunctionContract;

import java.util.ArrayList;
import java.util.List;

import static com.framgia.fdms.screen.producer.vendor.VendorViewModel.Action.ACTION_ADD_VENDOR;
import static com.framgia.fdms.screen.producer.vendor.VendorViewModel.Action.ACTION_DELETE_VENDOR;
import static com.framgia.fdms.screen.producer.vendor.VendorViewModel.Action.ACTION_EDIT_VENDOR;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.TAG_MAKER_DIALOG;

/**
 * Exposes the data to be used in the Vendor screen.
 */
public class VendorViewModel extends BaseObservable
    implements VendorContract.ViewModel, ProducerDialogContract.ActionCallback {
    private ProducerFunctionContract.ProducerPresenter mPresenter;
    private ListVendorAdapter mAdapter;
    private List<Producer> mVendors = new ArrayList<>();
    private AppCompatActivity mActivity;
    private ProducerDialog mVendorDialog;
    private int mPositionScroll = OUT_OF_INDEX;
    private Producer mVendorEdit;
    private int mTypeAction;
    private Producer mOldVendor;
    private boolean mIsLoadMore;
    private int mLoadingMoreVisibility;

    public VendorViewModel(Activity activity) {
        mActivity = (AppCompatActivity) activity;
        mAdapter = new ListVendorAdapter(FDMSApplication.getInstant(), this, mVendors);
        setLoadingMoreVisibility(View.GONE);
    }

    public void setLoadingMoreVisibility(int visibility) {
        mLoadingMoreVisibility = visibility;
        notifyPropertyChanged(BR.loadingMoreVisibility);
    }

    @Bindable
    public int getLoadingMoreVisibility() {
        return mLoadingMoreVisibility;
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
    public ListVendorAdapter getAdapter() {
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
    public void onActionError() {
        Toast.makeText(mActivity, mActivity.getString(R.string.error_opps), Toast.LENGTH_SHORT)
            .show();
        switch (mTypeAction) {
            case ACTION_ADD_VENDOR:
                mVendors.remove(0);
                mAdapter.notifyItemRemoved(0);
                break;
            case ACTION_DELETE_VENDOR:
                mVendors.add(mPositionScroll, mVendorEdit);
                mAdapter.notifyItemInserted(mPositionScroll);
                break;
            case ACTION_EDIT_VENDOR:
                mVendorEdit.setName(mOldVendor.getName());
                mVendorEdit.setDescription(mOldVendor.getDescription());
                break;
            default:
                break;
        }
    }

    @Override
    public void onEditProducerClick(Producer vendor) {
        mVendorDialog = ProducerDialog.newInstant(vendor, mActivity.getResources()
            .getString(R.string.action_edit), this);
        mVendorDialog.show(mActivity.getSupportFragmentManager(), TAG_MAKER_DIALOG);
    }

    @Override
    public void onDeleteProducerClick(final Producer vendor) {
        final int indexRemove = mVendors.indexOf(vendor);
        mVendors.remove(vendor);
        mAdapter.notifyItemRemoved(indexRemove);
        Snackbar.make(mActivity.findViewById(android.R.id.content), R.string.title_vendor_delete,
            Snackbar.LENGTH_LONG)
            .setAction(R.string.title_undo, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mVendors.add(indexRemove, vendor);
                    mAdapter.notifyItemInserted(indexRemove);
                    setPositionScroll(indexRemove);
                }
            })
            .addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    setPositionScroll(OUT_OF_INDEX);
                    ((VendorPresenter) mPresenter).deleteVendor(vendor);
                }
            })
            .show();
    }

    @Override
    public void onAddProducerClick() {
        mVendorDialog = ProducerDialog.newInstant(new Producer(), mActivity.getResources()
            .getString(R.string.title_add_producer), this);
        mVendorDialog.show(mActivity.getSupportFragmentManager(), TAG_MAKER_DIALOG);
    }

    @Override
    public void onAddCallback(Producer producer) {
        mVendors.add(0, producer);
        mAdapter.notifyItemInserted(0);
        setPositionScroll(0);
    }

    @Override
    public void onEditCallback(Producer oldProducer, Producer newProducer) {
        if (oldProducer == null || newProducer == null) return;
        oldProducer.setDescription(newProducer.getDescription());
        oldProducer.setName(newProducer.getName());
    }

    @Bindable
    public RecyclerView.OnScrollListener getScrollListener() {
        return mScrollListener;
    }

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
                ((VendorPresenter) mPresenter).getVendors();
            }
        }
    };

    @IntDef({ACTION_EDIT_VENDOR, ACTION_ADD_VENDOR, ACTION_DELETE_VENDOR})
    public @interface Action {
        int ACTION_EDIT_VENDOR = 0;
        int ACTION_ADD_VENDOR = 1;
        int ACTION_DELETE_VENDOR = 2;
    }

    protected VendorViewModel(Parcel in) {
        mPresenter = (VendorContract.Presenter) in.readValue(
            VendorContract.Presenter.class.getClassLoader());
        mAdapter = (ListVendorAdapter) in.readValue(ListVendorAdapter.class.getClassLoader());
        if (in.readByte() == 0x01) {
            mVendors = new ArrayList<Producer>();
            in.readList(mVendors, Producer.class.getClassLoader());
        } else {
            mVendors = null;
        }
        mActivity = (AppCompatActivity) in.readValue(AppCompatActivity.class.getClassLoader());
        mVendorDialog = (ProducerDialog) in.readValue(ProducerDialog.class.getClassLoader());
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
}