package com.framgia.fdms.screen.deviceselection;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.databinding.ItemDeviceSelectionBinding;
import java.util.List;

/**
 * Created by MyPC on 12/09/2017.
 */
public class DeviceSelectionAdapter
    extends BaseRecyclerViewAdapter<Device, DeviceSelectionAdapter.ViewHolder> {
    private OnRecyclerViewItemClickListener<Device> mItemClickListener;
    private List<Device> mDevices;

    DeviceSelectionAdapter(@NonNull Context context,
        OnRecyclerViewItemClickListener<Device> itemClickListener, List<Device> devices) {
        super(context);
        mItemClickListener = itemClickListener;
        mDevices = devices;
    }

    @Override
    public void addItem(List<Device> data) {
        if (data == null) {
            return;
        }
        mDevices.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        mDevices.clear();
        notifyDataSetChanged();
    }

    @Override
    public DeviceSelectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDeviceSelectionBinding binding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_device_selection, parent, false);
        return new DeviceSelectionAdapter.ViewHolder(binding, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(DeviceSelectionAdapter.ViewHolder holder, int position) {
        holder.bindData(mDevices.get(position));
    }

    @Override
    public int getItemCount() {
        return mDevices == null ? 0 : mDevices.size();
    }

    /**
     * ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemDeviceSelectionBinding mBinding;
        private OnRecyclerViewItemClickListener<Device> mItemClickListener;

        public ViewHolder(ItemDeviceSelectionBinding binding,
            BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Device> listener) {
            super(binding.getRoot());
            mBinding = binding;
            mItemClickListener = listener;
        }

        void bindData(Device device) {
            if (device == null) {
                return;
            }
            mBinding.setItem(device);
            mBinding.setListenner(mItemClickListener);
            mBinding.executePendingBindings();
        }
    }
}
