package com.framgia.fdms.screen.producer;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.framgia.fdms.FDMSApplication;
import com.framgia.fdms.R;
import com.framgia.fdms.data.model.Producer;
import com.framgia.fdms.databinding.DialogConfirmProducerBinding;

import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_ACTION_CALLBACK;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_PRODUCER;
import static com.framgia.fdms.utils.Constant.BundleConstant.BUNDLE_TITLE;

/**
 * Created by framgia on 04/07/2017.
 */
public class ProducerDialog extends DialogFragment implements ProducerDialogContract {
    private ObservableField<String> mMessageError = new ObservableField<>();
    private Producer mProducer, mTempProducer = new Producer();
    private ObservableField<String> mTitle = new ObservableField<>();
    private ProducerDialogContract.ActionCallback mActionCallback;
    private DialogConfirmProducerBinding mBinding;
    private static final String TITLE_EDIT = "Edit";
    private static final String TITLE_ADD = "Add";

    public static ProducerDialog newInstant(Producer producer, String title,
                                            ProducerDialogContract.ActionCallback callback) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_PRODUCER, producer);
        bundle.putString(BUNDLE_TITLE, title);
        bundle.putParcelable(BUNDLE_ACTION_CALLBACK, callback);
        ProducerDialog producerDialog = new ProducerDialog();
        producerDialog.setArguments(bundle);
        return producerDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mProducer = bundle.getParcelable(BUNDLE_PRODUCER);
        mActionCallback = bundle.getParcelable(BUNDLE_ACTION_CALLBACK);
        mTempProducer.setName(mProducer.getName());
        mTempProducer.setDescription(mProducer.getDescription());
        mTitle.set(bundle.getString(BUNDLE_TITLE));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mBinding =
            DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_confirm_producer, null, false);
        mBinding.setProducer(mTempProducer);
        mBinding.setDialog(this);
        builder.setView(mBinding.getRoot());
        return builder.create();
    }

    @Override
    public void onCancelClick() {
        dismiss();
    }

    @Override
    public void onSubmitClick() {
        if (TextUtils.isEmpty(mTempProducer.getName())) {
            mMessageError.set(FDMSApplication.getInstant()
                .getResources()
                .getString(R.string.msg_error_user_name));
            return;
        }
        dismiss();
        switch (mTitle.get()) {
            case TITLE_EDIT:
                mActionCallback.onEditCallback(mProducer, mTempProducer);
                break;
            case TITLE_ADD:
                mActionCallback.onAddCallback(mTempProducer);
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

    public void setTitle(ObservableField<String> title) {
        mTitle = title;
    }
}
