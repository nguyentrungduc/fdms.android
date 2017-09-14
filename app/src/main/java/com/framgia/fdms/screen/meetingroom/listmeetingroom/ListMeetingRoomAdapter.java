package com.framgia.fdms.screen.meetingroom.listmeetingroom;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.databinding.ItemListMeetingRoomBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinh on 08/09/2017.
 */

public class ListMeetingRoomAdapter
    extends BaseRecyclerViewAdapter<Producer, ListMeetingRoomAdapter.ViewHolder> {

    private OnRecyclerViewItemClickListener<Producer> mItemClickListener;
    private List<Producer> mMeetingRooms;

    ListMeetingRoomAdapter(@NonNull Context context) {
        super(context);
        mMeetingRooms = new ArrayList<>();
    }

    @Override
    public void onUpdatePage(List<Producer> data) {
        if (data == null) {
            return;
        }
        mMeetingRooms.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ListMeetingRoomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemListMeetingRoomBinding binding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_list_meeting_room, parent, false);
        return new ListMeetingRoomAdapter.ViewHolder(binding, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(ListMeetingRoomAdapter.ViewHolder holder, int position) {
        holder.bindData(mMeetingRooms.get(position));
    }

    @Override
    public int getItemCount() {
        return mMeetingRooms == null ? 0 : mMeetingRooms.size();
    }

    void setItemClickListener(OnRecyclerViewItemClickListener<Producer> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void clear() {
        mMeetingRooms.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemListMeetingRoomBinding mBinding;
        private OnRecyclerViewItemClickListener<Producer> mItemClickListener;

        public ViewHolder(ItemListMeetingRoomBinding binding,
            BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Producer> listener) {
            super(binding.getRoot());
            mBinding = binding;
            mItemClickListener = listener;
        }

        void bindData(Producer meetingRoom) {
            if (meetingRoom == null) {
                return;
            }
            mBinding.setViewModel(
                new ItemListMeetingRoomViewModel(meetingRoom, mItemClickListener));
            mBinding.executePendingBindings();
        }
    }
}
