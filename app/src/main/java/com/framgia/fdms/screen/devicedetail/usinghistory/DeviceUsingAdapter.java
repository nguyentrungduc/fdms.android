package com.framgia.fdms.screen.devicedetail.usinghistory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.data.model.NewDeviceUsingHistory;
import com.framgia.fdms.databinding.ItemDeviceUsingBinding;
import com.framgia.fdms.utils.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by framgia on 23/05/2017.
 */

public class DeviceUsingAdapter extends RecyclerView.Adapter<DeviceUsingAdapter.ViewHolder> {
    private static final String FORMAT_DATE = "%s -> %s";

    private Context mContext;
    private List<NewDeviceUsingHistory> mHistories = new ArrayList<>();
    private BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener mListener;

    public void setListener(BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) mContext = parent.getContext();
        ItemDeviceUsingBinding binding =
            ItemDeviceUsingBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ViewHolder(binding, mListener);
    }

    public void addData(List<NewDeviceUsingHistory> deviceUsingHistories) {
        if (deviceUsingHistories == null) return;
        mHistories.addAll(deviceUsingHistories);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mHistories.get(position));
    }

    @Override
    public int getItemCount() {
        return mHistories != null ? mHistories.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemDeviceUsingBinding mBinding;
        private BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener mListener;

        ViewHolder(ItemDeviceUsingBinding binding,
            BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mListener = listener;
        }

        public void bindData(NewDeviceUsingHistory item) {
            if (item == null) {
                return;
            }
            mBinding.setDeviceUsingHistory(item);
            mBinding.setTime(getStrFormatTime(item));
            mBinding.setListener(mListener);
            mBinding.executePendingBindings();
        }

        private String getStrFormatTime(NewDeviceUsingHistory deviceUsingHistory) {
            if (deviceUsingHistory == null) {
                return "";
            }
            return String.format(FORMAT_DATE,
                Utils.getStringDate(deviceUsingHistory.getBorrowDate(), mContext),
                Utils.getStringDate(deviceUsingHistory.getReturnDate(), mContext));
        }
    }
}
