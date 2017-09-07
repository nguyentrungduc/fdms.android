package com.framgia.fdms.screen.producer.marker;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.framgia.fdms.BR;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.screen.producer.ProducerDialog;
import com.framgia.fdms.screen.producer.ProducerDialogContract;
import com.framgia.fdms.screen.producer.ProducerFunctionContract;

import java.util.ArrayList;
import java.util.List;

import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.TAG_MAKER_DIALOG;

/**
 * Exposes the data to be used in the MarkerFragment screen.
 */
public class MarkerViewModel extends BaseObservable
    implements MarkerContract.ViewModel, FloatingSearchView.OnQueryChangeListener,
    ProducerDialogContract.ActionCallback {
    private ProducerFunctionContract.ProducerPresenter mPresenter;
    private MakerApdater mAdapter;
    private FragmentActivity mActivity;
    private ProducerDialog mDialog;
    private List<Producer> mMakers = new ArrayList<>();
    private int mPositionScroll = OUT_OF_INDEX;

    @Bindable
    public boolean isLoadMore() {
        return mIsLoadMore;
    }

    public void setLoadMore(boolean loadMore) {
        mIsLoadMore = loadMore;
        notifyPropertyChanged(BR.loadMore);
    }

    private boolean mIsLoadMore;

    public MarkerViewModel(FragmentActivity activity) {
        mActivity = activity;
        setAdapter(new MakerApdater(mMakers, this));
        setLoadMore(false);
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

    @Override
    public void onLoadMakerFail() {
        setLoadMore(false);
        Snackbar.make(mActivity.findViewById(android.R.id.content), R.string.error_load_makers,
            Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onLoadMakerSucessfully(List<Producer> makers) {
        if (makers != null) {
            mMakers.addAll(makers);
            mAdapter.notifyDataSetChanged();
        }
        setLoadMore(false);
    }

    @Bindable
    public MakerApdater getAdapter() {
        return mAdapter;
    }

    public void setAdapter(MakerApdater adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery) {
        //// TODO: 06/07/2017 later
    }

    @Override
    public void onEditProducerClick(Producer maker) {
        mDialog = ProducerDialog.newInstant(maker, mActivity.getResources()
            .getString(R.string.action_edit), this);
        mDialog.show(mActivity.getSupportFragmentManager(), TAG_MAKER_DIALOG);
    }

    @Override
    public void onDeleteProducerClick(final Producer maker) {
        if (mMakers == null || maker == null) {
            return;
        }
        final int indexRemove = mMakers.indexOf(maker);
        mMakers.remove(maker);
        mAdapter.notifyItemRemoved(indexRemove);
        Snackbar.make(mActivity.findViewById(android.R.id.content), R.string.title_vendor_delete,
            Snackbar.LENGTH_LONG)
            .setAction(R.string.title_undo, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMakers.add(indexRemove, maker);
                    mAdapter.notifyItemInserted(indexRemove);
                    setPositionScroll(indexRemove);
                }
            })
            .addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    setPositionScroll(OUT_OF_INDEX);
                    // TODO: next task
                }
            })
            .show();
    }

    @Override
    public void onAddProducerClick() {
        mDialog = ProducerDialog.newInstant(new Producer(), mActivity.getResources()
            .getString(R.string.title_add_producer), this);
        mDialog.show(mActivity.getSupportFragmentManager(), TAG_MAKER_DIALOG);
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
    public void onAddCallback(Producer maker) {
        if (mAdapter == null || maker == null) return;
        mAdapter.onAddItems(maker);
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
                setLoadMore(true);
                ((MarkerPresenter) mPresenter).getMakers(0, 0);
            }
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
