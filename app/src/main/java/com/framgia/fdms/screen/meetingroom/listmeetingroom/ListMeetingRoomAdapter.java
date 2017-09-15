package com.framgia.fdms.screen.meetingroom.listmeetingroom;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.databinding.ItemListMeetingRoomBinding;
import com.framgia.fdms.utils.navigator.Navigator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinh on 08/09/2017.
 */

public class ListMeetingRoomAdapter
    extends BaseRecyclerViewAdapter<Producer, ListMeetingRoomAdapter.ViewHolder> {

    private List<Producer> mMeetingRooms;
    private ListMeetingRoomViewModel mViewModel;

    ListMeetingRoomAdapter(Context context, ListMeetingRoomViewModel viewModel) {
        super(context);
        mMeetingRooms = new ArrayList<>();
        mViewModel = viewModel;
    }

    @Override
    public void onUpdatePage(List<Producer> data) {
        if (data == null) {
            return;
        }
        mMeetingRooms.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(int position, Producer producer) {
        if (producer == null || position < 0 || position > mMeetingRooms.size()) {
            return;
        }
        mMeetingRooms.add(position, producer);
        notifyItemInserted(position);
    }

    public void removeData(Producer producer) {
        if (mMeetingRooms == null || mMeetingRooms.size() == 0 || producer == null) {
            return;
        }
        mMeetingRooms.remove(producer);
        notifyItemRemoved(mMeetingRooms.indexOf(producer));
    }

    public void updateData(Producer oldMeeting, Producer newMeeting) {
        int pos = mMeetingRooms.indexOf(oldMeeting);
        if (pos < 0) {
            return;
        }
        mMeetingRooms.add(pos, newMeeting);
        notifyItemChanged(pos);
    }

    @Override
    public ListMeetingRoomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemListMeetingRoomBinding binding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_list_meeting_room, parent, false);
        return new ListMeetingRoomAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ListMeetingRoomAdapter.ViewHolder holder, int position) {
        holder.bindData(mMeetingRooms.get(position));
    }

    @Override
    public int getItemCount() {
        return mMeetingRooms == null ? 0 : mMeetingRooms.size();
    }

    public void clear() {
        mMeetingRooms.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemListMeetingRoomBinding mBinding;

        public ViewHolder(ItemListMeetingRoomBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindData(Producer meetingRoom) {
            if (meetingRoom == null) {
                return;
            }
            mBinding.setViewModel(mViewModel);
            mBinding.setItem(meetingRoom);
            mBinding.executePendingBindings();
        }
    }
}
