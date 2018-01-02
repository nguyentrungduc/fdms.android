package com.framgia.fdms.screen.requestcreation.requestfor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.fdms.data.model.AssigneeUser;
import com.framgia.fdms.databinding.ItemRequestForBinding;

import java.util.List;

/**
 * Created by ToanDoan
 * <></>
 */

public class SelectRequestForAdapter extends RecyclerView.Adapter<SelectRequestForAdapter.SelectionHolder> {

    private LayoutInflater mInflater;
    private List<AssigneeUser> mDatas;
    private SelectRequestForViewModel mViewModel;

    public SelectRequestForAdapter(List<AssigneeUser> datas) {
        mDatas = datas;
    }

    public void setViewModel(SelectRequestForViewModel viewModel) {
        mViewModel = viewModel;
    }

    public void updateData(List<AssigneeUser> datas) {
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
        ItemRequestForBinding binding =
                ItemRequestForBinding.inflate(mInflater, parent, false);
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
        private ItemRequestForBinding mBinding;

        public SelectionHolder(ItemRequestForBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(AssigneeUser user) {
            if (user == null) {
                return;
            }
            mBinding.setData(user);
            mBinding.setViewModel(mViewModel);
            mBinding.executePendingBindings();
        }
    }
}
