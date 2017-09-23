package com.framgia.fdms.screen.device.mydevice.mydevicedetail;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.databinding.ItemMyDeviceBinding;
import java.util.List;

/**
 * Created by toand on 9/22/2017.
 */

public class MyDeviceDetailAdapter extends RecyclerView.Adapter<MyDeviceDetailAdapter.ViewHolder> {
    private List<Device> mDevices;
    private MyDeviceDetailViewModel mViewModel;
    private LayoutInflater mLayoutInflater;

    public MyDeviceDetailAdapter(List<Device> devices, MyDeviceDetailViewModel viewModel) {
        mDevices = devices;
        mViewModel = viewModel;
    }

    public void updateData(List<Device> devices) {
        if (devices == null) {
            return;
        }
        mDevices.addAll(devices);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemMyDeviceBinding binding =
            DataBindingUtil.inflate(mLayoutInflater, R.layout.item_my_device, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mDevices.get(position));
    }

    @Override
    public int getItemCount() {
        return mDevices != null ? mDevices.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemMyDeviceBinding mBinding;

        public ViewHolder(ItemMyDeviceBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bindData(Device device) {
            if (device == null) {
                return;
            }
            mBinding.setDevice(device);
            mBinding.setViewModel(mViewModel);
            mBinding.executePendingBindings();
        }
    }
}
