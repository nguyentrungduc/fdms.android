package com.framgia.fdms.screen.selection;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.databinding.ItemNewStatusSelectionBinding;
import java.util.List;

import static com.framgia.fdms.screen.selection.SelectionType.ASSIGNEE;
import static com.framgia.fdms.screen.selection.SelectionType.BRANCH;
import static com.framgia.fdms.screen.selection.SelectionType.BRANCH_ALL;
import static com.framgia.fdms.screen.selection.SelectionType.CATEGORY;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_GROUP;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_GROUP_ALL;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_GROUP_DIALOG;
import static com.framgia.fdms.screen.selection.SelectionType.DEVICE_USING_HISTORY;
import static com.framgia.fdms.screen.selection.SelectionType.MARKER;
import static com.framgia.fdms.screen.selection.SelectionType.MEETING_ROOM;
import static com.framgia.fdms.screen.selection.SelectionType.RELATIVE_STAFF;
import static com.framgia.fdms.screen.selection.SelectionType.REQUEST_FOR;
import static com.framgia.fdms.screen.selection.SelectionType.STATUS;
import static com.framgia.fdms.screen.selection.SelectionType.STATUS_REQUEST;
import static com.framgia.fdms.screen.selection.SelectionType.USER_BORROW;
import static com.framgia.fdms.screen.selection.SelectionType.VENDOR;
import static com.framgia.fdms.utils.Constant.NONE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.TITLE_ALL;
import static com.framgia.fdms.utils.Constant.TITLE_NA;

/**
 * Created by ToanDoan
 * <></>
 */

public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.SelectionHolder> {

    private LayoutInflater mInflater;
    private List<Status> mDatas;
    private SelectionViewModel mViewModel;

    public SelectionAdapter(List<Status> datas) {
        mDatas = datas;
    }

    public void setViewModel(SelectionViewModel viewModel) {
        mViewModel = viewModel;
    }

    public void updateData(List<Status> datas, boolean isSearch, int selectedType) {
        if (mDatas == null || datas == null) {
            return;
        }
        initData(datas, selectedType, isSearch);
        notifyDataSetChanged();
    }

    private void initData(List<Status> statuses, int selectedType, boolean iSearch) {
        if (mDatas.size() == 0 && !iSearch) {
            switch (selectedType) {
                case STATUS:
                case VENDOR:
                case MARKER:
                case MEETING_ROOM:
                case DEVICE_GROUP:
                case BRANCH:
                case DEVICE_GROUP_DIALOG:
                case DEVICE_USING_HISTORY:
                case CATEGORY:
                    break;
                case BRANCH_ALL:
                case DEVICE_GROUP_ALL:
                case REQUEST_FOR:
                case STATUS_REQUEST:
                    mDatas.add(0, new Producer(OUT_OF_INDEX, TITLE_ALL));
                    break;
                case RELATIVE_STAFF:
                    mDatas.add(0, new Producer(OUT_OF_INDEX, TITLE_NA));
                    break;
                case ASSIGNEE:
                case USER_BORROW:
                    mDatas.add(0, new Producer(OUT_OF_INDEX, NONE));
                    break;
                default:
                    break;
            }
        }
        mDatas.addAll(statuses);
    }

    public void clearData() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    @Override
    public SelectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        ItemNewStatusSelectionBinding binding =
            ItemNewStatusSelectionBinding.inflate(mInflater, parent, false);
        return new SelectionHolder(binding);
    }

    @Override
    public void onBindViewHolder(SelectionHolder holder, int position) {
        holder.bind(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public List<Status> getDatas() {
        return mDatas;
    }

    public void setDatas(List<Status> datas) {
        mDatas = datas;
    }

    /**
     * Holder of adapter to display data
     */
    public class SelectionHolder extends RecyclerView.ViewHolder {
        private ItemNewStatusSelectionBinding mBinding;

        public SelectionHolder(ItemNewStatusSelectionBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(Status status) {
            if (status == null) {
                return;
            }
            mBinding.setData(status);
            mBinding.setViewModel(mViewModel);
            mBinding.executePendingBindings();
        }
    }
}
