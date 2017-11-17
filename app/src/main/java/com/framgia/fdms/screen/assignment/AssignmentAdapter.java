package com.framgia.fdms.screen.assignment;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.fdms.BR;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.databinding.ItemAssignmentBinding;
import com.framgia.fdms.screen.devicedetail.DeviceDetailActivity;
import com.framgia.fdms.utils.navigator.Navigator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPC on 09/06/2017.
 */
public class AssignmentAdapter
        extends BaseRecyclerViewAdapter<Device, AssignmentAdapter.ViewHolder> {
    private List<Device> mRequests;
    private AssignmentViewModel mViewModel;

    protected AssignmentAdapter(@NonNull Context context, @NonNull AssignmentViewModel viewModel) {
        super(context);
        mViewModel = viewModel;
        mRequests = new ArrayList<>();
    }

    public List<Device> getData() {
        return mRequests == null ? new ArrayList<Device>() : mRequests;
    }

    public void addItem(Device request) {
        for (Device requestTemp : mRequests) {
            if (requestTemp.getId() == request.getId()) {
                return;
            }
        }
        mRequests.add(request);
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        if (position >= 0 && position < mRequests.size()) {
            mRequests.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public void addItem(List<Device> data) {
        if (data == null) {
            return;
        }
        mRequests.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAssignmentBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_assignment, parent, false);
        return new ViewHolder(binding, mViewModel);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mRequests.get(position));
    }

    @Override
    public int getItemCount() {
        return mRequests == null ? 0 : mRequests.size();
    }

    /**
     * ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder  {
        private ItemAssignmentBinding mBinding;
        private AssignmentViewModel mViewModel;

        public ViewHolder(ItemAssignmentBinding binding, AssignmentViewModel viewModel) {
            super(binding.getRoot());
            mBinding = binding;
            mViewModel = viewModel;
        }

        void bindData(Device device) {
            if (device == null) {
                return;
            }
            mBinding.setDevice(device);
            mBinding.setViewModel(mViewModel);
            mBinding.setPosition(getAdapterPosition());
            mBinding.executePendingBindings();
        }
    }

}
