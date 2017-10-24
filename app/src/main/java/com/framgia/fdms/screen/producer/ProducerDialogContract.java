package com.framgia.fdms.screen.producer;

import android.os.Parcelable;
import com.framgia.fdms.data.model.Producer;

/**
 * Created by beepi on 07/07/2017.
 */
public interface ProducerDialogContract {
    void onCancelClick();

    void onSubmitClick();

    /**
     * this interface implement all methods to solve data
     * after pressing submiting button of dialog
     */
    interface ActionCallback extends Parcelable {
        void onAddCallback(Producer producer, Producer tempGroupDevice);

        void onEditCallback(Producer oldProducer, Producer newProducer, Producer tempGroupType);

        void onChooseGroupTypeClickDialog(Producer tempDeviceCategory, Producer tempGroupType);
    }
}
