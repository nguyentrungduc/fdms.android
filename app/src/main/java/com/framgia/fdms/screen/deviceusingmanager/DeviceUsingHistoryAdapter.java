package com.framgia.fdms.screen.deviceusingmanager;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Device;
import com.framgia.fdms.data.model.DeviceUsingHistory;
import com.framgia.fdms.databinding.ItemDeviceUsingHistoryBinding;
import com.framgia.fdms.databinding.ItemGroupUserDeviceBinding;
import java.util.List;

/**
 * Created by lamvu on 06/09/2017.
 */

public class DeviceUsingHistoryAdapter extends BaseExpandableListAdapter {

    private List<DeviceUsingHistory> mDevices;
    private DeviceUsingManagerViewModel mViewModel;

    public DeviceUsingHistoryAdapter(List<DeviceUsingHistory> deviceUsingHistories) {
        mDevices = deviceUsingHistories;
    }

    public void clearData() {
        if (mDevices != null) {
            mDevices.clear();
            notifyDataSetChanged();
        }
    }

    public void updateData(List<DeviceUsingHistory> devices) {
        if (devices == null) {
            return;
        }
        mDevices.addAll(devices);
        notifyDataSetChanged();
    }

    public void setViewModel(DeviceUsingManagerViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public int getGroupCount() {
        return mDevices != null ? mDevices.size() : 0;
    }

    @Override
    public int getChildrenCount(int i) {
        if (getGroup(i) != null && getGroup(i).getUsingDevices() != null) {
            return getGroup(i).getUsingDevices().size();
        }
        return 0;
    }

    @Override
    public DeviceUsingHistory getGroup(int i) {
        return mDevices.get(i);
    }

    @Override
    public Device getChild(int groupPos, int childPos) {
        if (getGroup(groupPos) != null && getGroup(groupPos).getUsingDevices() != null) {
            return getGroup(groupPos).getUsingDevices().get(childPos);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPos) {
        return groupPos;
    }

    @Override
    public long getChildId(int groupPos, int childPos) {
        return childPos;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view,
        ViewGroup viewGroup) {
        ItemGroupUserDeviceBinding binding;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            binding = DataBindingUtil.inflate(inflater, R.layout.item_group_user_device, viewGroup,
                false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (ItemGroupUserDeviceBinding) view.getTag();
        }

        binding.setIsExpanded(isExpanded);
        binding.setDeviceUsingHistory(getGroup(groupPosition));
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    public View getChildView(int groupPosition, int childPos, boolean b, View view,
        ViewGroup viewGroup) {
        ItemDeviceUsingHistoryBinding binding;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            binding =
                DataBindingUtil.inflate(inflater, R.layout.item_device_using_history, viewGroup,
                    false);
            view = binding.getRoot();
            view.setTag(binding);
        } else {
            binding = (ItemDeviceUsingHistoryBinding) view.getTag();
        }
        binding.setDevice(getChild(groupPosition, childPos));
        binding.setViewModel(mViewModel);
        binding.executePendingBindings();
        return binding.getRoot();
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
