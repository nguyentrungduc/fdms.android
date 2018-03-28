package com.framgia.fdms.screen.inventorydevice;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.databinding.ItemInventoryDeviceBinding;

import java.util.List;

/**
 * Created by Sony on 3/27/2018.
 */

public class InventoryDeviceAdapter extends
        RecyclerView.Adapter<InventoryDeviceAdapter.InventoryDeviceViewHolder> {

    private List<Device> mDevices;
    private OnDeviceListenner mOnDeviceListenner;

    public InventoryDeviceAdapter(List<Device> devices, Context context) {
        mDevices = devices;
    }

    public void addDevices(List<Device> devices){
        mDevices.addAll(mDevices);
        notifyDataSetChanged();
    }

    public List<Device> getDevices() {
        return mDevices;
    }

    @Override
    public InventoryDeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemInventoryDeviceBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_inventory_device, parent, false);
        return new InventoryDeviceViewHolder(binding, mOnDeviceListenner);

    }

    public void setDeviceClickListener(OnDeviceListenner onDeviceListenner) {
        mOnDeviceListenner = onDeviceListenner;
    }

    @Override
    public void onBindViewHolder(InventoryDeviceViewHolder holder, int position) {
        holder.binding(mDevices.get(position));
    }

    @Override
    public int getItemCount() {
        return mDevices == null ? 0 : mDevices.size();
    }

    public class InventoryDeviceViewHolder extends RecyclerView.ViewHolder {
        private OnDeviceListenner mOnDeviceListenner;
        private ItemInventoryDeviceBinding mBinding;

        public InventoryDeviceViewHolder(View itemView, OnDeviceListenner onDeviceListenner) {
            super(itemView);
            mOnDeviceListenner = onDeviceListenner;
        }

        public InventoryDeviceViewHolder(ItemInventoryDeviceBinding binding, OnDeviceListenner
                onDeviceListenner) {
            super(binding.getRoot());
            mBinding = binding;
            mOnDeviceListenner = onDeviceListenner;
        }

        public void binding(Device device) {
            mBinding.executePendingBindings();
            mBinding.setViewModel(new ItemDeviceInventoryViewModel(mOnDeviceListenner, device,
                    getAdapterPosition()));
        }
    }

}
