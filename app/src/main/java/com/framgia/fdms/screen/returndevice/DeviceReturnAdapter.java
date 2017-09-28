package com.framgia.fdms.screen.returndevice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.databinding.ItemDeviceReturnBinding;
import java.util.List;

/**
 * Created by Hoang Van Nha on 5/22/2017.
 * <></>
 */

public class DeviceReturnAdapter
    extends RecyclerView.Adapter<DeviceReturnAdapter.DeviceReturnHolder> {

    private final ReturnDeviceContract.ViewModel mViewModel;
    private List<Device> mDevices;
    private LayoutInflater mInflater;

    public DeviceReturnAdapter(ReturnDeviceViewModel viewModel, List<Device> devices) {
        mDevices = devices;
        mViewModel = viewModel;
    }

    public boolean updateWithDeviceCode(List<Device> devices, String deviceCode) {
        boolean isExit = false;
        for (Device item : devices) {
            if (item.getDeviceCode().equals(deviceCode)) {
                item.setSelected(true);
                isExit = true;
                break;
            }
        }
        mDevices.addAll(devices);
        notifyDataSetChanged();
        return isExit;
    }

    public boolean updateScanDevice(String deviceCode) {
        boolean isExit = false;
        for (int i = 0; i < mDevices.size(); i++) {
            Device item = mDevices.get(i);
            if (item.getDeviceCode().equals(deviceCode)) {
                item.setSelected(true);
                notifyItemChanged(i);
                isExit = true;
                break;
            }
        }
        return isExit;
    }

    @Override
    public DeviceReturnHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) mInflater = LayoutInflater.from(parent.getContext());
        ItemDeviceReturnBinding binding = ItemDeviceReturnBinding.inflate(mInflater, parent, false);
        binding.setViewModel(mViewModel);
        return new DeviceReturnHolder(binding);
    }

    @Override
    public void onBindViewHolder(DeviceReturnHolder holder, int position) {
        Device device = mDevices.get(position);
        if (device != null) holder.bind(device, position);
    }

    @Override
    public int getItemCount() {
        return mDevices == null ? 0 : mDevices.size();
    }

    public class DeviceReturnHolder extends RecyclerView.ViewHolder {

        private final ItemDeviceReturnBinding mBinding;

        public DeviceReturnHolder(ItemDeviceReturnBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Device device, int position) {
            mBinding.setDevice(device);
            mBinding.setPosition(position);
            mBinding.executePendingBindings();
        }
    }

    public List<Device> getDevices() {
        return mDevices;
    }
}
