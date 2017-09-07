package com.framgia.fdms.screen.devicehistory;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.databinding.ItemDeviceHistoryBinding;
import com.framgia.fdms.databinding.ItemGroupDeviceHistoryBinding;

import java.util.List;

/**
 * Created by framgia on 14/07/2017.
 */
public class ListUserAdapter extends BaseExpandableListAdapter {
    private List<DeviceUsingHistory> mDeviceUsingHistories;
    private DeviceHistoryContract.ViewModel mViewModel;

    public ListUserAdapter(DeviceHistoryContract.ViewModel viewModel, List<DeviceUsingHistory>
        deviceUsingHistoryList) {
        mViewModel = viewModel;
        mDeviceUsingHistories = deviceUsingHistoryList;
    }

    @Override
    public int getGroupCount() {
        return mDeviceUsingHistories == null ? 0 : mDeviceUsingHistories.size();
    }

    @Override
    public int getChildrenCount(int i) {
        List<Device> deviceUsingHistories = mDeviceUsingHistories.get(i)
            .getUsingDevices();
        return deviceUsingHistories == null ? 0 : deviceUsingHistories.size();
    }

    @Override
    public Object getGroup(int i) {
        return mDeviceUsingHistories.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        List<Device> deviceUsingHistories = mDeviceUsingHistories.get(i)
            .getUsingDevices();
        return deviceUsingHistories.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean isExpanded, View view, ViewGroup viewGroup) {
        ItemGroupDeviceHistoryBinding binding;
        if (view == null) {
            binding =
                DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                    R.layout.item_group_device_history, viewGroup, false);
            binding.setViewModel((DeviceHistoryViewModel) mViewModel);
            ((DeviceHistoryViewModel) mViewModel).setExpanded(isExpanded);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (ItemGroupDeviceHistoryBinding) view.getTag();
            binding.setDeviceHistory(mDeviceUsingHistories.get(i));
        }
        binding.setDeviceHistory(mDeviceUsingHistories.get(i));
        ((DeviceHistoryViewModel) mViewModel).setExpanded(isExpanded);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ItemDeviceHistoryBinding binding = DataBindingUtil
            .inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_device_history,
                viewGroup, false);
        binding.setDevice(mDeviceUsingHistories.get(i).getUsingDevices().get(i1));
        return binding.getRoot();
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
