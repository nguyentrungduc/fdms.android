package com.framgia.fdms.screen.producer;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.databinding.ItemProducerBinding;
import java.util.List;

/**
 * Created by framgia on 03/07/2017.
 */

public class ProducerAdapter extends BaseRecyclerViewAdapter<Producer, ProducerAdapter.ViewHolder> {
    private List<Producer> mProducers;
    private ProducerContract.ViewModel mViewModel;

    public ProducerAdapter(@NonNull Context context, @NonNull ProducerContract.ViewModel viewModel,
        @NonNull List<Producer> producers) {
        super(context);
        mViewModel = viewModel;
        mProducers = producers;
    }

    public void onUpdateItem(Producer old, Producer producer) {
        int pos = mProducers.indexOf(old);
        if (pos < 0) {
            return;
        }
        mProducers.add(pos, producer);
        mProducers.remove(pos + 1);
        notifyItemChanged(pos);
    }

    @Override
    public void onUpdatePage(List<Producer> datas) {
        if (datas == null) {
            return;
        }
        mProducers.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearData() {
        if (mProducers == null) {
            return;
        }
        mProducers.clear();
        notifyDataSetChanged();
    }

    public void addData(int position, Producer producer) {
        if (producer == null || position < 0 || position > mProducers.size()) {
            return;
        }
        mProducers.add(position, producer);
        notifyItemInserted(position);
    }

    public void removeData(Producer producer) {
        if (mProducers == null || mProducers.size() == 0 || producer == null) {
            return;
        }
        int index = mProducers.indexOf(producer);
        if (index < 0) {
            return;
        }
        mProducers.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public ProducerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemProducerBinding binding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_producer, parent, false);
        binding.setViewModel((ProducerViewModel) mViewModel);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ProducerAdapter.ViewHolder holder, int position) {
        holder.bindData(mProducers.get(position));
    }

    @Override
    public int getItemCount() {
        return mProducers == null ? 0 : mProducers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemProducerBinding mBinding;

        public ViewHolder(ItemProducerBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindData(Producer producer) {
            if (producer == null) {
                return;
            }
            mBinding.setProducer(producer);
            mBinding.executePendingBindings();
        }
    }
}
