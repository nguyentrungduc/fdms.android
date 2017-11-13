package com.framgia.fdms.screen.deviceselection.filter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.databinding.ItemSelectDeviceFilterBinding;

import java.util.List;

/**
 * All request status id
 */

public class BottomFilterAdapter extends RecyclerView.Adapter<BottomFilterAdapter.ViewHolder>
        implements BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Status> {
    private List<Producer> mData;
    private BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener mListener;
    private int mSelectedPos = 0;

    public BottomFilterAdapter(List<Producer> data,
                               BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener listener) {
        mData = data;
        mListener = listener;
    }

    public void updateData(List<Producer> producers) {
        if (producers == null) {
            return;
        }
        mData.addAll(producers);
        notifyDataSetChanged();
    }

    public void updateData(int index, Producer producer) {
        if (producer == null) {
            return;
        }
        mData.add(index, producer);
        notifyDataSetChanged();
    }

    public int getSelectedItemId() {
        if (mData == null || mData.size() == 0 || mSelectedPos < 0) {
            return -1;
        }
        return mData.get(mSelectedPos).getId();
    }

    public void updateData(Producer producer) {
        if (producer == null) {
            return;
        }
        mData.add(producer);
        notifyDataSetChanged();
    }

    public void setSelectedPos(int selectedPos) {
        mSelectedPos = selectedPos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSelectDeviceFilterBinding binding =
                ItemSelectDeviceFilterBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public void onItemRecyclerViewClick(Status item) {
        if (mListener != null) {
            mListener.onItemRecyclerViewClick(item);
        }
        mSelectedPos = mData.indexOf(item);
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemSelectDeviceFilterBinding mBinding;

        public ViewHolder(ItemSelectDeviceFilterBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bindData(Status status) {
            mBinding.setCurentPos(getAdapterPosition());
            mBinding.setListenner(BottomFilterAdapter.this);
            mBinding.setModel(status);
            mBinding.setSelectedPos(mSelectedPos);
            mBinding.executePendingBindings();
        }
    }
}