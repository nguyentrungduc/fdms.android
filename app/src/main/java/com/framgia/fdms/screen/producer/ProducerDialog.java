package com.framgia.fdms.screen.producer;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.framgia.fdms.FDMSApplication;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.databinding.DialogConfirmProducerBinding;

import static com.framgia.fdms.screen.producer.ProducerDialog.Type.ADD;
import static com.framgia.fdms.screen.producer.ProducerDialog.Type.EDIT;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_ACTION_CALLBACK;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_DEVICE_GROUP_TYPE;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_PRODUCER;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_PRODUCER_DIALOG_TYPE;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_PRODUCER_TYPE;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_SHOW_GROUP_DEVICE;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_TITLE;
import static com.framgia.fdms.utils.Constant.OUT_OF_INDEX;

/**
 * Created by framgia on 04/07/2017.
 */
public class ProducerDialog extends DialogFragment implements ProducerDialogContract {
    private ObservableField<String> mMessageError = new ObservableField<>();
    private Producer mProducer, mTempProducer = new Producer(OUT_OF_INDEX, "");
    private Producer mDeviceGroup;

    private ProducerDialogContract.ActionCallback mActionCallback;
    private DialogConfirmProducerBinding mBinding;
    private Boolean mIsShowGroupType;

    private ObservableField<String> mTitle = new ObservableField<>();
    private ObservableField<String> mHintName = new ObservableField<>();

    @ProducerType
    private int mProducerType;
    @ProducerDialog.Type
    private int mDialogType;
    
    public static ProducerDialog newInstant(Producer producer, @ProducerType int producerType,
                                            @ProducerDialog.Type int dialogType,
                                            ProducerDialogContract.ActionCallback callback,
                                            Producer deviceGroupType, boolean showGroupDevice) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(BUNDLE_SHOW_GROUP_DEVICE, showGroupDevice);
        bundle.putParcelable(BUNDLE_PRODUCER, producer);
        bundle.putParcelable(BUNDLE_DEVICE_GROUP_TYPE, deviceGroupType);
        bundle.putInt(BUNDLE_PRODUCER_TYPE, producerType);
        bundle.putInt(BUNDLE_PRODUCER_DIALOG_TYPE, dialogType);
        bundle.putParcelable(BUNDLE_ACTION_CALLBACK, callback);
        ProducerDialog producerDialog = new ProducerDialog();
        producerDialog.setArguments(bundle);
        return producerDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        getDataBundle();
        initDialogTitle();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout
                .dialog_confirm_producer, null, false);
        mBinding.setProducer(mTempProducer);
        mBinding.setDialog(this);
        builder.setView(mBinding.getRoot());
        return builder.create();
    }

    private void getDataBundle() {
        Bundle bundle = getArguments();
        mProducer = bundle.getParcelable(BUNDLE_PRODUCER);
        mDeviceGroup = bundle.getParcelable(BUNDLE_DEVICE_GROUP_TYPE);
        mActionCallback = bundle.getParcelable(BUNDLE_ACTION_CALLBACK);
        mIsShowGroupType = bundle.getBoolean(BUNDLE_SHOW_GROUP_DEVICE);
        mProducerType = bundle.getInt(BUNDLE_PRODUCER_TYPE);
        mDialogType = bundle.getInt(BUNDLE_PRODUCER_DIALOG_TYPE);
        mTempProducer.setName(mProducer.getName());
        mTempProducer.setDescription(mProducer.getDescription());
        mTempProducer.setId(mProducer.getId());
    }

    private void initDialogTitle() {
        Context context = getContext();
        switch (mProducerType) {
            case ProducerType.VENDOR:
                setTitle(mDialogType == ADD ? context.getString(R.string.title_add_new_vendor) :
                        context.getString(R.string.title_edit_vendor));
                setHintName(context.getString(R.string.title_vendor_name));
                break;
            case ProducerType.MARKER:
                setTitle(mDialogType == ADD ? context.getString(R.string.title_add_new_maker) :
                        context.getString(R.string.title_edit_maker));
                setHintName(context.getString(R.string.title_maker_name));
                break;
            case ProducerType.MEETING_ROOM:
                setTitle(mDialogType == ADD ? context.getString(R.string
                        .title_add_new_meeting_room) : context.getString(R.string
                        .title_edit_meeting_room));
                setHintName(context.getString(R.string.title_meeting_room_name));
                break;
            case ProducerType.DEVICE_GROUPS:
                setTitle(mDialogType == ADD ? context.getString(R.string
                        .title_add_new_device_group) : context.getString(R.string
                        .title_edit_device_group));
                setHintName(context.getString(R.string.title_device_group_name));
                break;
            case ProducerType.CATEGORIES_GROUPS:
                setTitle(mDialogType == ADD ? context.getString(R.string
                        .title_add_new_device_category) : context.getString(R.string
                        .title_edit_device_category));
                setHintName(context.getString(R.string.title_device_category_name));
                break;
            default:
                break;
        }
    }

    @Override
    public void onCancelClick() {
        dismiss();
    }

    @Override
    public void onSubmitClick() {
        if (TextUtils.isEmpty(mTempProducer.getName())) {
            mMessageError.set(FDMSApplication.getInstant().getResources().getString(R.string
                    .msg_error_user_name));
            return;
        }
        dismiss();
        switch (mDialogType) {
            case EDIT:
                mActionCallback.onEditCallback(mProducer, mTempProducer, mDeviceGroup);
                break;
            case ADD:
                mActionCallback.onAddCallback(mTempProducer, mDeviceGroup);
                break;
        }
    }

    public ObservableField<String> getMessageError() {
        return mMessageError;
    }

    public void setMessageError(ObservableField<String> messageError) {
        mMessageError = messageError;
    }

    public ObservableField<String> getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle.set(title);
    }

    public void onChooseGroupTypeClick() {
        if (mDialogType == EDIT){
            return;
        }
        dismiss();
        mActionCallback.onChooseGroupTypeClickDialog(mTempProducer, mDeviceGroup);
    }

    public Producer getDeviceGroup() {
        return mDeviceGroup;
    }

    public void setDeviceGroup(Producer deviceGroup) {
        mDeviceGroup = deviceGroup;
    }

    public Boolean getIsShowGroupType() {
        return mIsShowGroupType;
    }

    public ObservableField<String> getHintName() {
        return mHintName;
    }

    public void setHintName(String hintName) {
        mHintName.set(hintName);
    }

    @IntDef({ADD, EDIT})
    @interface Type {
        int ADD = 0;
        int EDIT = 1;
    }
}
