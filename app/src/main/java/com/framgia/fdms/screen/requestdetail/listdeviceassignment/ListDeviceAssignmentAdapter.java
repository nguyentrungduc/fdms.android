package com.framgia.fdms.screen.requestdetail.listdeviceassignment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Request;
import com.framgia.fdms.databinding.ItemListDeviceAssignmentBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 04/10/2017.
 */

public class ListDeviceAssignmentAdapter
        extends BaseRecyclerViewAdapter<Request.DeviceRequest, ListDeviceAssignmentAdapter.ViewHolder> {

    private List<Request.DeviceRequest> mDeviceRequests;
    private ListDeviceAssignmentViewModel mViewModel;
    private BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener mListenner;

    protected ListDeviceAssignmentAdapter(@NonNull Context context,
                                          ListDeviceAssignmentViewModel viewModel) {
        super(context);
        mViewModel = viewModel;
        mDeviceRequests = new ArrayList<>();
    }

    public void setListenner(OnRecyclerViewItemClickListener listenner) {
        mListenner = listenner;
    }

    @Override
    public void addItem(List<Request.DeviceRequest> data) {
        if (data == null) {
            return;
        }
        mDeviceRequests.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemListDeviceAssignmentBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_list_device_assignment, parent, false);
        return new ListDeviceAssignmentAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mDeviceRequests.get(position));
    }

    @Override
    public int getItemCount() {
        return mDeviceRequests == null ? 0 : mDeviceRequests.size();
    }

    public void clear() {
        mDeviceRequests.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemListDeviceAssignmentBinding mBinding;

        public ViewHolder(ItemListDeviceAssignmentBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindData(Request.DeviceRequest deviceRequest) {
            if (deviceRequest == null) {
                return;
            }
            mBinding.setViewModel(mViewModel);
            mBinding.setItem(deviceRequest);
            mBinding.setListenner(mListenner);
            mBinding.executePendingBindings();
        }
    }
}
