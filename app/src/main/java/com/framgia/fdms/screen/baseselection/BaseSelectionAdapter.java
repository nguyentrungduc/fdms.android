package com.framgia.fdms.screen.baseselection;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.databinding.ItemStatusSelectionBinding;

import java.util.List;

/**
 * Created by ToanDoan
 * <></>
 */

public class BaseSelectionAdapter extends RecyclerView.Adapter<BaseSelectionAdapter.SelectionHolder> {

    private LayoutInflater mInflater;
    private List<Status> mDatas;
    private BaseSelectionViewModel mViewModel;

    public BaseSelectionAdapter(List<Status> datas) {
        mDatas = datas;
    }

    public void setViewModel(BaseSelectionViewModel viewModel) {
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
        ItemStatusSelectionBinding binding =
                ItemStatusSelectionBinding.inflate(mInflater, parent, false);
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
        private ItemStatusSelectionBinding mBinding;

        public SelectionHolder(ItemStatusSelectionBinding binding) {
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
