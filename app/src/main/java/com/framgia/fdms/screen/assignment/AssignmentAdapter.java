package com.framgia.fdms.screen.assignment;

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
import com.framgia.fdms.data.model.AssignmentRequest;
import com.framgia.fdms.databinding.ItemAssignmentBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPC on 09/06/2017.
 */
public class AssignmentAdapter
    extends BaseRecyclerViewAdapter<AssignmentRequest, AssignmentAdapter.ViewHolder> {
    private List<AssignmentRequest> mRequests;

    private AssignmentViewModel mViewModel;

    protected AssignmentAdapter(@NonNull Context context, @NonNull AssignmentViewModel viewModel) {
        super(context);
        mViewModel = viewModel;
        mRequests = new ArrayList<>();
    }

    public List<AssignmentRequest> getData() {
        return mRequests == null ? new ArrayList<AssignmentRequest>() : mRequests;
    }

    public void addItem(AssignmentRequest request) {
        mRequests.add(request);
        notifyDataSetChanged();
    }

    @Override
    public void onUpdatePage(List<AssignmentRequest> data) {
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemAssignmentBinding mBinding;
        private AssignmentViewModel mViewModel;

        public ViewHolder(ItemAssignmentBinding binding, AssignmentViewModel viewModel) {
            super(binding.getRoot());
            mBinding = binding;
            mViewModel = viewModel;
        }

        void bindData(AssignmentRequest request) {
            if (request == null) {
                return;
            }
            ViewHolderModel model =
                new ViewHolderModel(request, mViewModel, this, getAdapterPosition());
            mBinding.setViewHolderModel(model);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() >= 0 && getAdapterPosition() < mRequests.size()) {
                mRequests.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition(), mRequests.size());
            }
        }
    }

    public class ViewHolderModel extends BaseObservable {
        private AssignmentRequest mRequest;
        private AssignmentViewModel mViewModel;
        private View.OnClickListener mOnDeleteClick;
        private int mPosition;

        public ViewHolderModel(AssignmentRequest request, AssignmentViewModel viewModel,
            View.OnClickListener onDelteClick, int position) {
            mRequest = request;
            mViewModel = viewModel;
            mOnDeleteClick = onDelteClick;
            mPosition = position;
        }

        public AssignmentRequest getRequest() {
            return mRequest;
        }

        public void setRequest(AssignmentRequest request) {
            mRequest = request;
        }

        @Bindable
        public AssignmentViewModel getViewModel() {
            return mViewModel;
        }

        public void setViewModel(AssignmentViewModel viewModel) {
            mViewModel = viewModel;
            notifyPropertyChanged(BR.viewModel);
        }

        @Bindable
        public View.OnClickListener getOnDeleteClick() {
            return mOnDeleteClick;
        }

        public void setOnDeleteClick(View.OnClickListener onDeleteClick) {
            mOnDeleteClick = onDeleteClick;
            notifyPropertyChanged(BR.onDeleteClick);
        }

        @Bindable
        public int getPosition() {
            return mPosition;
        }

        public void setPosition(int position) {
            mPosition = position;
            notifyPropertyChanged(BR.position);
        }
    }
}
