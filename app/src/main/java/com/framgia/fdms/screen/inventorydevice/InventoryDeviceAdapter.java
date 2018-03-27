package com.framgia.fdms.screen.inventorydevice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.databinding.ItemDeviceReturnBinding;
import com.framgia.fdms.screen.returndevice.DeviceReturnAdapter;
import com.framgia.fdms.screen.returndevice.ReturnDeviceContract;
import com.framgia.fdms.screen.returndevice.ReturnDeviceViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sony on 3/27/2018.
 */

public class InventoryDeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Device> mDevices;
    private Context mContext;

    public InventoryDeviceAdapter(List<Device> devices, Context context) {
        mDevices = devices;
        mContext = context;
    }

    public void addDevices(List<Device> devices){
        mDevices.addAll(mDevices);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    public class InventoryDeviceViewHolder extends RecyclerView.ViewHolder {
        OnDeviceListenner mOnDeviceListenner;
        Ite

        public InventoryDeviceViewHolder(View itemView, OnDeviceListenner onDeviceListenner) {
            super(itemView);
            mOnDeviceListenner = onDeviceListenner;
        }

        public void binding() {

        }
    }
}
