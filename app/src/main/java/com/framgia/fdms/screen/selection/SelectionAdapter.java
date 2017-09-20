package com.framgia.fdms.screen.selection;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.databinding.ItemNewStatusSelectionBinding;
import java.util.List;

/**
 * Created by ToanDoan
 * <></>
 */

public class SelectionAdapter
    extends RecyclerView.Adapter<SelectionAdapter.SelectionHolder> {

    private LayoutInflater mInflater;
    private List<Status> mDatas;
    private SelectionViewModel mViewModel;

    public SelectionAdapter(List<Status> datas) {
        mDatas = datas;
    }

    public void setViewModel(SelectionViewModel viewModel) {
        mViewModel = viewModel;
    }

    public void updateData(List<Status> datas) {
        if (mDatas == null || datas == null) {
            return;
        }
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearData() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    @Override
    public SelectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        ItemNewStatusSelectionBinding binding =
            ItemNewStatusSelectionBinding.inflate(mInflater, parent, false);
        return new SelectionHolder(binding);
    }

    @Override
    public void onBindViewHolder(SelectionHolder holder, int position) {
        holder.bind(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    /**
     * Holder of adapter to display data
     */
    public class SelectionHolder extends RecyclerView.ViewHolder {
        private ItemNewStatusSelectionBinding mBinding;

        public SelectionHolder(ItemNewStatusSelectionBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(Status status) {
            if (status == null) {
                return;
            }
            mBinding.setData(status);
            mBinding.setViewModel(mViewModel);
            mBinding.executePendingBindings();
        }
    }
}
