package com.framgia.fdms.screen.deviceselection.filter;

import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.fdms.BR;
import com.framgia.fdms.BaseRecyclerViewAdapter;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.data.model.Status;
import com.framgia.fdms.data.source.CategoryRepository;
import com.framgia.fdms.data.source.DeviceGroupRepository;
import com.framgia.fdms.data.source.api.error.BaseException;
import com.framgia.fdms.data.source.api.error.RequestError;
import com.framgia.fdms.data.source.remote.CategoryRemoteDataSource;
import com.framgia.fdms.databinding.DeviceBottomDialogBinding;
import com.framgia.fdms.utils.navigator.Navigator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.framgia.fdms.utils.Constant.FIRST_PAGE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;
import static com.framgia.fdms.utils.Constant.PER_PAGE;

/**
 * All request status id
 */


public class BottomFilterDialog extends BaseObservable {
    private AppCompatActivity mActivity;
    private BottomSheetDialog mBottomSheetDialog;
    private BottomFilterAdapter mCategoryAdapter;
    private BottomFilterAdapter mGroupAdapter;
    private DeviceGroupRepository mGroupRepository;
    private CategoryRepository mCategoryRepository;

    private Status mCategory;
    private Status mGroup;

    private int mPage = FIRST_PAGE;

    private Navigator mNavigator;

    public BottomFilterDialog(AppCompatActivity activity) {
        mActivity = activity;
        mNavigator = new Navigator(mActivity);
        mGroupRepository = DeviceGroupRepository.getInstance();
        mCategoryRepository = CategoryRepository.getInstance(CategoryRemoteDataSource.getInstance());
        init();
    }

    private void init() {
        mBottomSheetDialog = new BottomSheetDialog(mActivity);
        DeviceBottomDialogBinding binding =
                DeviceBottomDialogBinding.inflate(LayoutInflater.from(mActivity), null, false);


        mCategoryAdapter = new BottomFilterAdapter(new ArrayList<Producer>(), null);
        mGroupAdapter = new BottomFilterAdapter(new ArrayList<Producer>(), mOnItemGroupClicked);

        mBottomSheetDialog.setContentView(binding.getRoot());

        updateDefaultAdapter();

        getGroup();
    }

    private void updateDefaultAdapter() {
        updateDefaultCategoryAdapter();
        updateDefaultGroupAdapter();
    }

    private void updateDefaultCategoryAdapter() {
        if (mCategoryAdapter.getItemCount() == 0) {
            mCategory = new Producer(OUT_OF_INDEX,
                    mActivity.getString(R.string.action_all));
            mCategoryAdapter.updateData((Producer) mCategory);
        }
    }

    private void updateDefaultGroupAdapter() {
        if (mGroupAdapter.getItemCount() == 0) {
            mGroup = new Producer(OUT_OF_INDEX,
                    mActivity.getString(R.string.action_all));
            mGroupAdapter.updateData((Producer) mGroup);
        }
    }

    private void getGroup() {
        mGroupRepository.getListDeviceGroup("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Producer>>() {
                    @Override
                    public void accept(List<Producer> producers) throws Exception {
                        updateDefaultAdapter();
                        mGroupAdapter.updateData(producers);
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mNavigator.showToast(error.getMessage());
                    }
                });
    }

    private void getCategory() {
        if (mGroup == null) {
            return;
        }
        mCategoryRepository.getListCategory("", mGroup.getId(), mPage, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Producer>>() {
                    @Override
                    public void accept(List<Producer> producers) throws Exception {
                        updateDefaultAdapter();
                        mCategoryAdapter.updateData(producers);
                        if (producers.size() == PER_PAGE) {
                            mPage++;
                            getCategory();
                        }
                    }
                }, new RequestError() {
                    @Override
                    public void onRequestError(BaseException error) {
                        mNavigator.showToast(error.getMessage());
                    }
                });

    }

    private BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Status> mOnItemGroupClicked =
            new BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Status>() {

                @Override
                public void onItemRecyclerViewClick(Status item) {
                    if (mGroup.getId() == item.getId()) {
                        return;
                    }
                    mGroup = item;
                    mCategoryAdapter.setSelectedPos(0);

                    if (mGroup.getId() == OUT_OF_INDEX) {
                        mCategoryAdapter.clearData();
                        updateDefaultCategoryAdapter();
                        return;
                    }


                    mCategoryAdapter.clearData();
                    mPage = FIRST_PAGE;
                    getCategory();
                }
            };

    public void show() {
        if (mBottomSheetDialog != null && !mBottomSheetDialog.isShowing()) {
            mBottomSheetDialog.show();
        }
    }

    public void dissmiss() {
        if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) {
            mBottomSheetDialog.dismiss();
        }
    }

    @Bindable
    public BottomFilterAdapter getCategoryAdapter() {
        return mCategoryAdapter;
    }

    public void setCategoryAdapter(BottomFilterAdapter categoryAdapter) {
        mCategoryAdapter = categoryAdapter;
        notifyPropertyChanged(BR.categoryAdapter);
    }

    @Bindable
    public BottomFilterAdapter getGroupAdapter() {
        return mGroupAdapter;
    }

    public void setGroupAdapter(BottomFilterAdapter groupAdapter) {
        mGroupAdapter = groupAdapter;
        notifyPropertyChanged(BR.groupAdapter);
    }

}