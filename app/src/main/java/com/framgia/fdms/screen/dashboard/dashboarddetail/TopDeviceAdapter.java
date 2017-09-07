package com.framgia.fdms.screen.dashboard.dashboarddetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.databinding.ItemDashBoardTopDeviceBinding;
import com.framgia.fdms.screen.device.OnDeviceClickListenner;

import java.util.List;

/**
 * Created by MyPC on 30/05/2017.
 */
public class TopDeviceAdapter extends BaseRecyclerViewAdapter<Device, TopDeviceAdapter.ViewHolder> {
    private List<Device> mDevices;
    private OnDeviceClickListenner mListenner;

    protected TopDeviceAdapter(@NonNull Context context, OnDeviceClickListenner listenner,
                               @NonNull List<Device> devices) {
        super(context);
        mDevices = devices;
        mListenner = listenner;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDashBoardTopDeviceBinding binding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_dash_board_top_device, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onUpdatePage(List<Device> data) {
        if (data == null) {
            return;
        }
        mDevices.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(TopDeviceAdapter.ViewHolder holder, int position) {
        holder.bindData(mDevices.get(position));
    }

    @Override
    public int getItemCount() {
        return mDevices == null ? 0 : mDevices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemDashBoardTopDeviceBinding mBinding;

        public ViewHolder(ItemDashBoardTopDeviceBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindData(Device device) {
            if (device == null) {
                return;
            }
            mBinding.setDevice(device);
            mBinding.setListenner(mListenner);
            mBinding.executePendingBindings();
        }
    }

    public void clear() {
        if (mDevices != null) mDevices.clear();
        notifyDataSetChanged();
    }
}
