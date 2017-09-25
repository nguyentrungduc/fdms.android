package com.framgia.fdms.screen.meetingroom.detailmeetingroom;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.databinding.ItemListDeviceRoomDetailBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 11/09/2017.
 */

public class ListDeviceAdapter
    extends BaseRecyclerViewAdapter<Device, ListDeviceAdapter.ViewHolder> {

    private OnRecyclerViewItemClickListener<Device> mItemClickListener;
    private List<Device> mDevices;

    ListDeviceAdapter(@NonNull Context context) {
        super(context);
        mDevices = new ArrayList<>();
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
    public ListDeviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemListDeviceRoomDetailBinding binding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_list_device_room_detail, parent, false);
        return new ListDeviceAdapter.ViewHolder(binding, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(ListDeviceAdapter.ViewHolder holder, int position) {
        holder.bindData(mDevices.get(position));
    }

    @Override
    public int getItemCount() {
        return mDevices == null ? 0 : mDevices.size();
    }

    void setItemClickListener(OnRecyclerViewItemClickListener<Device> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void clear() {
        mDevices.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemListDeviceRoomDetailBinding mBinding;
        private OnRecyclerViewItemClickListener<Device> mItemClickListener;

        public ViewHolder(ItemListDeviceRoomDetailBinding binding,
            BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Device> listener) {
            super(binding.getRoot());
            mBinding = binding;
            mItemClickListener = listener;
        }

        void bindData(Device device) {
            if (device == null) {
                return;
            }
            mBinding.setViewModel(new ItemListDeviceViewModel(device, mItemClickListener));
            mBinding.executePendingBindings();
        }
    }
}
