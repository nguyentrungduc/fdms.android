package com.framgia.fdms.screen.devicedetail.history;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.DeviceHistoryDetail;
import com.framgia.fdms.databinding.ItemHistoryDetailBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoang Van Nha on 5/23/2017.
 * <></>
 */

public class DeviceDetailHistoryAdapter
    extends RecyclerView.Adapter<DeviceDetailHistoryAdapter.HistoryDetailHolder> {
    private List<DeviceHistoryDetail> mData;
    private LayoutInflater mInflater;
    private BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener mListener;

    public DeviceDetailHistoryAdapter() {
        mData = new ArrayList<>();
    }

    public void addData(List<DeviceHistoryDetail> deviceHistoryDetails) {
        if (deviceHistoryDetails == null) {
            return;
        }
        mData.addAll(deviceHistoryDetails);
        notifyDataSetChanged();
    }

    public void setListener(BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public HistoryDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        ItemHistoryDetailBinding binding =
            DataBindingUtil.inflate(mInflater, R.layout.item_history_detail, parent, false);
        return new HistoryDetailHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(HistoryDetailHolder holder, int position) {
        DeviceHistoryDetail deviceHistoryDetail = mData.get(position);
        holder.bind(deviceHistoryDetail, position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class HistoryDetailHolder extends RecyclerView.ViewHolder {

        private ItemHistoryDetailBinding mBinding;
        private BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener mListener;

        public HistoryDetailHolder(ItemHistoryDetailBinding binding,
            BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mListener = listener;
        }

        private void bind(DeviceHistoryDetail deviceHistoryDetail, int position) {
            mBinding.setDeviceHistoryDetail(deviceHistoryDetail);
            mBinding.setPosition(position);
            mBinding.setListener(mListener);
            mBinding.executePendingBindings();
        }
    }
}
