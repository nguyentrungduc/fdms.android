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

    public DeviceUsingHistoryAdapter(List<DeviceUsingHistory> deviceUsingHistories) {
        mDevices = deviceUsingHistories;
    }

    @Override
    public int getGroupCount() {
        return mDevices != null ? mDevices.size() : 0;
    }

    @Override
    public int getChildrenCount(int i) {
        List<Device> devices = mDevices.get(i).getUsingDevices();
        return devices != null ? devices.size() : 0;
    }

    @Override
    public Object getGroup(int i) {
        return mDevices.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        List<Device> devices = mDevices.get(i).getUsingDevices();
        return devices.get(i);
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
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ItemGroupUserDeviceBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.item_group_user_device, viewGroup, false);
            view = binding.getRoot();
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ItemDeviceUsingHistoryBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.item_device_using_history, viewGroup, false);
            view = binding.getRoot();
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
